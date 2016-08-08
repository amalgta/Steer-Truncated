package com.styx.steer.Client.Activity.connection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.styx.steer.Client.Connection.Connection;
import com.styx.steer.Client.R;


public abstract class ConnectionEditActivity extends AppCompatActivity implements OnClickListener
{
	public static Connection connectionParam;

	private Connection connection;

	private Button save;

	private EditText name;
	private EditText password;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(getLayoutId());
		this.connection = connectionParam;

		this.save = (Button) this.findViewById(R.id.save);
		this.save.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD)
		{
			// Don't need the Save button on newer devices
			LayoutParams a = (LayoutParams) this.save.getLayoutParams();
			a.height = 0;
			this.save.setLayoutParams(a);
			this.save.forceLayout();
		}

		this.name = (EditText) this.findViewById(R.id.name);
		this.password = (EditText) this.findViewById(R.id.password);

	}

	protected abstract int getLayoutId();

	protected void onResume()
	{
		super.onResume();

		this.name.setText(this.connection.getName());
		this.password.setText(this.connection.getPassword());
	}

	protected void onPause()
	{
		super.onPause();

		this.connection.setName(this.name.getText().toString());
		this.connection.setPassword(this.password.getText().toString());
	}

	public void onClick(View v)
	{
		if (v == this.save)
		{
			this.finish();
		}
	}
}
