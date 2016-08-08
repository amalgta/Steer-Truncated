package com.styx.steer.Client.Activity.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFloat;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.Bluetooth.BluetoothChecker;
import com.styx.steer.Client.Connection.Connection;
import com.styx.steer.Client.Connection.ConnectionBluetooth;
import com.styx.steer.Client.Connection.ConnectionList;
import com.styx.steer.Client.Connection.ConnectionWifi;
import com.styx.steer.Client.R;

public class ConnectionListActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener, OnClickListener
{
    // private static final int NEW_MENU_ITEM_ID = 0;

    private Steer application;
    ListView lView;
    private ConnectionList connections;

    private ConnectionListAdapter adapter;

    private AlertDialog alertDialogNew;

    private AlertDialog alertDialogItemLongClick;

    private int itemLongClickPosition;

    private Toolbar mToolbar;

    ButtonFloat newButtonFloat;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.application = (Steer) this.getApplication();

        this.connections = this.application.getConnections();

        adapter = new ConnectionListAdapter(this, this.connections);
        setContentView(R.layout.connection_list);
        lView = (ListView) findViewById(R.id.conn_list);
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(this);
        lView.setOnItemLongClickListener(this);
        newButtonFloat = (ButtonFloat) findViewById(R.id.add);
        newButtonFloat.setRippleSpeed(8);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void add(View v)
    {
        initAlertDialogNew();
    }

    protected void onResume()
    {
        super.onResume();

        this.refresh();

        if (this.connections.getCount() == 0)
        {
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                            .text(R.string.text_no_connection) // text to be displayed
                            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                    , this);
        }
    }

    protected void onPause()
    {
        super.onPause();
        this.connections.save();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        // Inflate the menu items for use in the action bar
        android.view.MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connection_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            initAlertDialogItemLongClick();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_new:
                initAlertDialogNew();
                return true;
            case R.id.action_edit:
                // Don't try to edit non-selected activity
                if (this.connections.getUsedPosition() < 0)
                    return true;
                this.connections.get(this.connections.getUsedPosition()).edit(this);
                return true;
            case R.id.action_remove:
                // Don't try to remove non-selected activity
                if (this.connections.getUsedPosition() < 0)
                    return true;
                this.connections.remove(this.connections.getUsedPosition());
                this.refresh();
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        this.useConnection(position);
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        this.itemLongClickPosition = position;

        initAlertDialogItemLongClick();

        return true;
    }

    public void onClick(DialogInterface dialog, int which)
    {
        if (dialog == this.alertDialogNew)
        {
            this.addConnection(which);
        }
        else if (dialog == this.alertDialogItemLongClick)
        {
            this.menu(which);
        }
    }

    private void menu(int which)
    {
        Connection connection = this.connections.get(this.itemLongClickPosition);

        switch (which)
        {
            case 0:
                this.useConnection(this.itemLongClickPosition);
                break;
            case 1:
                connection.edit(this);
                break;
            case 2:
                this.removeConnection();
                break;
        }
    }

    private void addConnection(int which)
    {
        if (which == Connection.BLUETOOTH && !BluetoothChecker.isBluetoohAvailable())
        {
            this.application.showToast(R.string.text_bluetooth_not_available_version);
        }
        else
        {
            Connection connection = this.connections.add(which);

            this.refresh();

            connection.edit(this);
        }
    }

    private void useConnection(int position)
    {
        this.connections.use(position);
        this.refresh();
    }

    private void removeConnection()
    {
        this.connections.remove(this.itemLongClickPosition);
        this.refresh();
    }

    private void refresh()
    {
        this.connections.sort();
        this.adapter.notifyDataSetChanged();
    }

    private void init()
    {
        this.initAlertDialogNew();

        this.initAlertDialogItemLongClick();
    }

    private void initAlertDialogNew()
    {
        String[] connectionTypeName = {
                this.getResources().getString(R.string.text_wifi), this.getResources().getString(R.string.text_bluetooth)
        };

        new MaterialDialog.Builder(this)
                .title(R.string.text_connection_type)
                .items(R.array.connection)
                .titleColorRes(R.color.connections)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                   addConnection(which);
                    }
                })
                .show();
    }

    private void initAlertDialogItemLongClick()
    {
        new MaterialDialog.Builder(this)
                .items(R.array.connection_action_name)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        menu(which);
                    }
                })
                .show();
    }

    private class ConnectionListAdapter extends BaseAdapter
    {
        private ConnectionList connections;
        private LayoutInflater layoutInflater;

        private int connectionUsedPosition;

        public ConnectionListAdapter(Context context, ConnectionList connections)
        {
            this.connections = connections;

            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.connectionUsedPosition = this.connections.getUsedPosition();
        }

        public void notifyDataSetChanged()
        {
            super.notifyDataSetChanged();

            this.connectionUsedPosition = this.connections.getUsedPosition();
        }

        public int getCount()
        {
            return this.connections.getCount();
        }

        public Connection getItem(int position)
        {
            return this.connections.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ConnectionViewHolder holder;

            if (convertView == null)
            {
                holder = new ConnectionViewHolder();

                convertView = this.layoutInflater.inflate(R.layout.connection, null);

                holder.use = (RadioButton) convertView.findViewById(R.id.use);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ConnectionViewHolder) convertView.getTag();
            }

            Connection connection = this.connections.get(position);

            holder.use.setChecked(position == this.connectionUsedPosition);

            if (connection instanceof ConnectionWifi)
            {
                holder.icon.setImageResource(R.drawable.wifi);
            }
            else if (connection instanceof ConnectionBluetooth)
            {
                holder.icon.setImageResource(R.drawable.bluetooth);
            }

            holder.name.setText(connection.getName());

            return convertView;
        }

        private class ConnectionViewHolder
        {
            public RadioButton use;
            public ImageView icon;
            public TextView name;
        }
    }
}
