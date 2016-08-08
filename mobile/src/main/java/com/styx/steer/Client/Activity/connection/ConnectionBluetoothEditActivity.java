package com.styx.steer.Client.Activity.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.styx.steer.Client.Connection.ConnectionBluetooth;
import com.styx.steer.Client.R;

public class ConnectionBluetoothEditActivity extends ConnectionEditActivity implements OnClickListener
{
	private static final int ADDRESS_REQUEST_CODE = 0;
	
	private ConnectionBluetooth connection;
	
	private EditText address;
	private Button edit;
	private Toolbar mToolbar;
	
	    @Override
    protected int getLayoutId(){
        return R.layout.connectionbluetoothedit;
    }
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.connection = (ConnectionBluetooth) connectionParam;
		this.address = (EditText) this.findViewById(R.id.address);
		
		this.edit = (Button) this.findViewById(R.id.edit);
		this.edit.setOnClickListener(this);
	}

    public void Save(View v){
        this.finish();
    }
	protected void onResume()
	{
		super.onResume();
		
		this.address.setText(this.connection.getAddress());
	}
	
	protected void onPause()
	{
		super.onPause();
		
		this.connection.setAddress(this.address.getText().toString());
	}
	
	public void onClick(View v)
	{
		super.onClick(v);
		
		if (v == this.edit)
		{
			this.startActivityForResult(new Intent(this, BluetoothDevicesActivity.class), ADDRESS_REQUEST_CODE);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        // Inflate the menu items for use in the action bar
        android.view.MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connection_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK)
		{
			if (requestCode == ADDRESS_REQUEST_CODE)
			{
				this.connection.setAddress(data.getStringExtra("address"));
			}
		}
	}
}
