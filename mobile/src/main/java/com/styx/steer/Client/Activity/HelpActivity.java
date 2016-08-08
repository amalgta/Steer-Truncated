package com.styx.steer.Client.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.styx.steer.Client.R;


public class HelpActivity extends MainActivity
{
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.help, null, false);
		mDrawer.addView(contentView, 0);
        mTitle.setText("Help");
        startIntroAnimation();
	}

    protected void onPause()
    {
        super.onPause();
        finish();
    }

    public void tutorial(View v){
        startActivity(new Intent(HelpActivity.this, Tutorial.class));
    }
}
