package com.styx.steer.Client.Activity.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.Connection.Connection;
import com.styx.steer.Client.Connection.ConnectionBluetooth;
import com.styx.steer.Client.Connection.ConnectionList;
import com.styx.steer.Client.Connection.ConnectionWifi;
import com.styx.steer.Client.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ConnectionListActivity extends AppCompatActivity {
    final int mFloatingActionMenuDelay = 400;
    private RecyclerView recyclerView;
    private ConnectionsAdapter adapter;
    private ConnectionList connectionList;
    private Steer mApplication;
    private Handler mUiHandler = new Handler();
    private FloatingActionMenu addConnectionMenu;
    private FloatingActionButton addWifi;
    private FloatingActionButton addBluetooth;

    @Override
    protected void onPause() {
        super.onPause();
        if (connectionList != null) {
            connectionList.save();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectionList != null) {
            adapter.notifyDataSetChanged();
        }
        if (connectionList.getCount() == 0) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!addConnectionMenu.isOpened())
                        addConnectionMenu.toggle(true);
                }
            }, mFloatingActionMenuDelay);

        }
    }

    private void validate(ArrayList<Object> validateList, View buttonView) {
        boolean FLAG = true;
        for (Object currentText : validateList) {
            Log.e("GTA_DEBUG", currentText.toString());
            if (currentText instanceof EditText) {
                if (!(((EditText) currentText).getText().toString().trim().length() > 0)) {
                    FLAG = false;
                    break;
                }
            } else if (currentText instanceof mConnection) {
                if ((((mConnection) currentText).getHost() == null)) {
                    FLAG = false;
                    break;
            }
        }
        buttonView.setEnabled(FLAG);
    }
    }

    private void editWifiConnection(final ConnectionWifi editedConnection, final int position) {
        final boolean add = (position == connectionList.getCount());
        String title, positiveText;
        if (add) {
            title = "Add Connection";
            positiveText = "ADD";
        } else {
            title = "Edit Connection";
            positiveText = "EDIT";
        }

        MaterialDialog dialog = new MaterialDialog.Builder(ConnectionListActivity.this)
                .title(title)
                .customView(R.layout.dialog_addwificonnection, true)
                .positiveText(positiveText)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        editedConnection.setName(((EditText) dialog.getCustomView().findViewById(R.id.connection_name)).getText().toString());
                        editedConnection.setPassword(((EditText) dialog.getCustomView().findViewById(R.id.connection_password)).getText().toString());
                        editedConnection.setHost(((EditText) dialog.getCustomView().findViewById(R.id.connection_address)).getText().toString());
                        editedConnection.setPort(Integer.parseInt(((EditText) dialog.getCustomView().findViewById(R.id.connection_port)).getText().toString()));
                        if (add) {
                            connectionList.add(editedConnection);
                            adapter.notifyItemInserted(connectionList.getCount());
                            addConnectionMenu.toggle(true);
                        } else {
                            adapter.notifyItemChanged(position);
                        }
                    }
                }).build();
        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        final EditText connectionName = (EditText) dialog.getCustomView().findViewById(R.id.connection_name);
        final EditText connection_password = (EditText) dialog.getCustomView().findViewById(R.id.connection_password);
        final EditText connection_address = (EditText) dialog.getCustomView().findViewById(R.id.connection_address);
        final EditText connection_port = (EditText) dialog.getCustomView().findViewById(R.id.connection_port);


        final ArrayList<Object> validateList = new ArrayList<Object>(Arrays.asList(connectionName, connection_password, connection_address, connection_port));
        positiveAction.setEnabled(false); // disabled by default
        if (!add) {
            connectionName.setText(editedConnection.getName());
            connection_password.setText(editedConnection.getPassword());
            connection_address.setText(editedConnection.getHost());
            connection_port.setText(String.valueOf(editedConnection.getPort()));
            validate(validateList, positiveAction);
        }
        connectionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        connection_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        connection_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        connection_port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                connection_password.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                connection_password.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                connection_password.setSelection(connection_password.getText().length());
            }
        });
        MDTintHelper.setTint(checkbox, ContextCompat.getColor(ConnectionListActivity.this, R.color.myAccentColor));
        MDTintHelper.setTint(connection_password, ContextCompat.getColor(ConnectionListActivity.this, R.color.myAccentColor));
        connectionName.setSelection(connectionName.getText().length());
        connection_password.setSelection(connection_password.getText().length());
        connection_address.setSelection(connection_address.getText().length());
        connection_port.setSelection(connection_port.getText().length());
        dialog.show();
    }

    private void editBluetoothConnection(final ConnectionBluetooth editedConnection, final int position) {
        final boolean add = (position == connectionList.getCount());
        final mConnection connection_host = new mConnection();
        String title, positiveText;
        if (add) {
            title = "Add Connection";
            positiveText = "ADD";
        } else {
            title = "Edit Connection";
            positiveText = "EDIT";
        }

        MaterialDialog dialog = new MaterialDialog.Builder(ConnectionListActivity.this)
                .title(title)
                .customView(R.layout.dialog_addbluetoothconnection, true)
                .positiveText(positiveText)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        editedConnection.setName(((EditText) dialog.getCustomView().findViewById(R.id.connection_name)).getText().toString());
                        editedConnection.setPassword(((EditText) dialog.getCustomView().findViewById(R.id.connection_password)).getText().toString());
                        editedConnection.setAddress(connection_host.getHost().toString());
                        if (add) {
                            connectionList.add(editedConnection);
                            adapter.notifyItemInserted(connectionList.getCount());
                            addConnectionMenu.toggle(true);
                        } else {
                            adapter.notifyItemChanged(position);
                        }
                    }
                }).build();
        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        final EditText connectionName = (EditText) dialog.getCustomView().findViewById(R.id.connection_name);
        final EditText connection_password = (EditText) dialog.getCustomView().findViewById(R.id.connection_password);



        /* Bluetooth */
        final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        final ArrayList<String> mBluetoothDevices = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mBluetoothDevices.add(device.getName() + "\n" + device.getAddress());
            }
        }

        final MaterialSpinner connection_device = (MaterialSpinner) dialog.getCustomView().findViewById(R.id.connection_device);
        connection_device.setItems(mBluetoothDevices);
        if (mBluetoothDevices.size() == 0) {

        }
        connection_device.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                connection_host.setHost(mBluetoothDevices.get(position).substring(mBluetoothDevices.get(position).length() - 17));
            }
        });

        final ArrayList<Object> validateList = new ArrayList<>(Arrays.asList(connectionName, connection_password, connection_host));
        positiveAction.setEnabled(false); // disabled by default
        if (!add) {
            connectionName.setText(editedConnection.getName());
            connection_password.setText(editedConnection.getPassword());
            connection_device.setSelectedIndex(2);
            //  connection_address.setText(editedConnection.getHost());
            //  connection_port.setText(String.valueOf(editedConnection.getPort()));
            validate(validateList, positiveAction);
        }
        connectionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        connection_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate(validateList, positiveAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                connection_password.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                connection_password.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                connection_password.setSelection(connection_password.getText().length());
            }
        });
        MDTintHelper.setTint(checkbox, ContextCompat.getColor(ConnectionListActivity.this, R.color.myAccentColor));
        MDTintHelper.setTint(connection_password, ContextCompat.getColor(ConnectionListActivity.this, R.color.myAccentColor));
        connectionName.setSelection(connectionName.getText().length());
        connection_password.setSelection(connection_password.getText().length());
        dialog.show();

        // connection_address.setSelection(connection_address.getText().length());
        // connection_port.setSelection(connection_port.getText().length());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mApplication = (Steer) this.getApplication();
        connectionList = mApplication.getConnections();
        adapter = new ConnectionsAdapter(this, connectionList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Floating Action bar Menu */
        addConnectionMenu = (FloatingActionMenu) findViewById(R.id.menu_red);
        addWifi = (FloatingActionButton) findViewById(R.id.addWifi);
        addBluetooth = (FloatingActionButton) findViewById(R.id.addBluetooth);
        addConnectionMenu.hideMenuButton(false);
        addConnectionMenu.setClosedOnTouchOutside(true);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addConnectionMenu.showMenuButton(true);
            }
        }, mFloatingActionMenuDelay);
        addBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBluetoothConnection((ConnectionBluetooth) connectionList.newConnection(Connection.BLUETOOTH), connectionList.getCount());
            }
        });

        addWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ConnectionListActivity.this, "ASD", Toast.LENGTH_SHORT).show();
                editWifiConnection((ConnectionWifi) connectionList.newConnection(Connection.WIFI), connectionList.getCount());
            }
        });


        addConnectionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConnectionMenu.toggle(true);
            }
        });


    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class mConnection {
        String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.MyViewHolder> {
        private Context mContext;
        private ConnectionList connectionList;

        public ConnectionsAdapter(Context mContext, ConnectionList connectionList) {
            this.mContext = mContext;
            this.connectionList = connectionList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.connection_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Connection connection = connectionList.get(position);
            holder.connection_name.setText(connection.getName());
            holder.connection_ip.setText(connection.getAddress());
            Glide.with(mContext).load(connection.getThumbnail()).into(holder.connection_thumbnail);
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow, holder.getAdapterPosition());
                }
            });

        }

        private void showPopupMenu(View view, int position) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.connection_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
            popup.show();
        }

        @Override
        public int getItemCount() {
            return this.connectionList.getCount();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView connection_name, connection_ip;
            public ImageView connection_thumbnail, overflow;
            private MyViewHolder theHolder;

            public MyViewHolder(View view) {
                super(view);
                connection_name = (TextView) view.findViewById(R.id.connection_name);
                connection_ip = (TextView) view.findViewById(R.id.connection_ip);
                connection_thumbnail = (ImageView) view.findViewById(R.id.connection_thumbnail);
                overflow = (ImageView) view.findViewById(R.id.overflow);
                theHolder = this;
            }
        }
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
            int position;
            public MyMenuItemClickListener(int position) {
                this.position = position;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_remove:
                        final Connection removedConnection = connectionList.get(position);
                        notifyItemRemoved(position);
                        connectionList.remove(position);

                        //notifyDataSetChanged();

                        final Snackbar removeNotifierSnackbar = Snackbar
                                .make(findViewById(R.id.main_content), removedConnection.getName() + " is deleted" + position, Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        connectionList.add(position, removedConnection);
                                        //notifyDataSetChanged();
                                        notifyItemInserted(position);
                                        Snackbar restoreNotifierSnackbar = Snackbar.make(findViewById(R.id.main_content), removedConnection.getName() + " is restored!" + position, Snackbar.LENGTH_SHORT);
                                        ((TextView) restoreNotifierSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.YELLOW);
                                        restoreNotifierSnackbar.show();
                                        // View sb=restoreNotifierSnackbar.getView();
                                    }
                                });
                        removeNotifierSnackbar.setActionTextColor(Color.RED);
                        ((TextView) removeNotifierSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.YELLOW);
                        removeNotifierSnackbar.show();
                        //View sbView = removeNotifierSnackbar.getView();
                        //TextView textView = (TextView) removeNotifierSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                        return true;
                    case R.id.action_edit:
                        final Connection editedConnection = connectionList.get(position);
                        if (editedConnection instanceof ConnectionWifi) {
                            editWifiConnection((ConnectionWifi) editedConnection, position);
                        } else {
                            editBluetoothConnection((ConnectionBluetooth) editedConnection, position);
                        }

                        return true;
                    default:
                }
                return false;
            }
        }
    }
}