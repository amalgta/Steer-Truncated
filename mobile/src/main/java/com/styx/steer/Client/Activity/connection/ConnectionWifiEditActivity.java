package com.styx.steer.Client.Activity.connection;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import com.styx.steer.Client.Connection.ConnectionWifi;
import com.styx.steer.Client.R;


public class ConnectionWifiEditActivity extends ConnectionEditActivity implements OnClickListener
{
	private ConnectionWifi connection;
	private EditText host;
	private EditText port;
	Button scan;
	ListView lv;
	private Toolbar mToolbar;

	@Override
	protected int getLayoutId(){
		return R.layout.connectionwifiedit;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
//		this.setContentView(R.layout.connectionwifiedit);
		super.onCreate(savedInstanceState);
		lv = (ListView) findViewById(android.R.id.list);
		this.connection = (ConnectionWifi) connectionParam;
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.host = (EditText) this.findViewById(R.id.host);
		this.port = (EditText) this.findViewById(R.id.port);
		SnackbarManager.show(
				Snackbar.with(getApplicationContext()) // context
						.type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
						.text(R.string.tip) // text to be displayed
						.duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
				, this);
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

	public void Save(View v){
		this.finish();
	}


	@Override
	public void onClick(View v)
	{

	}

	protected void onResume()
	{
		super.onResume();

		this.host.setText(this.connection.getHost());
		this.port.setText(Integer.toString(this.connection.getPort()));
	}

	protected void onPause()
	{
		super.onPause();

		this.connection.setHost(this.host.getText().toString());
		this.connection.setPort(Integer.parseInt(this.port.getText().toString()));
	}

}
