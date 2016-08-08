package com.styx.steer.Client.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.styx.steer.Client.R;


public class About extends MainActivity
{
	static String TAG = "steer";
	String mySkypeUri = "skype:arulnadhandev?chat";

	public static Intent getOpenFacebookIntent(Context context) {
		try {
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/1497673553"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/arulnadhan"));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.about, null, false);
		mDrawer.addView(contentView, 0);

        mTitle.setText("About");
        startIntroAnimation();


    }

    public void facebook(View v){
        startActivity(getOpenFacebookIntent(getApplicationContext()));
    }

    public void mail(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("text/plain");
						i.putExtra(Intent.EXTRA_EMAIL, new String[] {
								"amalgta@gmail.com"
						});
						Log.d(TAG, "GM is clicked");
						i.putExtra(Intent.EXTRA_SUBJECT, "steer");
						// i.putExtra(Intent.EXTRA_TEXT, "body of email");
						try
						{
							startActivity(Intent.createChooser(i, "Send mail..."));
						}
						catch (android.content.ActivityNotFoundException ex)
						{
							Toast.makeText(About.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
						}
    }

	public void skype(View v) {
		initiateSkypeUri(getApplicationContext(), mySkypeUri);
	}

	/**
	 * Initiate the actions encoded in the specified URI.
	 */
	public void initiateSkypeUri(Context myContext, String mySkypeUri)
	{

		// Make sure the Skype for Android client is installed
		if (!isSkypeClientInstalled(myContext))
		{
			goToMarket(myContext);
			return;
		}

		// Create the Intent from our Skype URI
		Uri skypeUri = Uri.parse(mySkypeUri);
		Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

		// Restrict the Intent to being handled by the Skype for Android client
		// only
		myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Initiate the Intent. It should never fail since we've already
		// established the
		// presence of its handler (although there is an extremely minute window
		// where that
		// handler can go away...)
		startActivity(myIntent);

		return;
	}

	public void goToMarket(Context myContext)
	{
		Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
		Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myContext.startActivity(myIntent);

		return;
	}

	public boolean isSkypeClientInstalled(Context myContext)
	{
		PackageManager myPackageMgr = myContext.getPackageManager();
		try
		{
			myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return (false);
		}
		return (true);
	}

    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
