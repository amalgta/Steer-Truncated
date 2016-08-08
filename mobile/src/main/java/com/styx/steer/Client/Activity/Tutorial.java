package com.styx.steer.Client.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.styx.steer.Client.R;


public class Tutorial extends Activity implements OnClickListener
{
	ImageView help;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.styx.steerclient.activity.MainActivity#onCreate(android.os.
	 * Bundle)
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		setContentView(R.layout.helpscreen);
		super.onCreate(savedInstanceState);
		help = (ImageView) findViewById(R.id.heelp);
		help.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (v == help)
		{
			finish();
		}
		
	}
}
