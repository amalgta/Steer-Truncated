package com.styx.steer.Client.Activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private static final String[] tabFloatPreferences = {
	        "control_sensitivity", "control_acceleration", "control_immobile_distance", "screenCapture_cursor_size", "buttons_size", "wheel_bar_width"
	};
	private static final String[] tabIntPreferences = {
	        "control_click_delay", "control_hold_delay"
	};
	
	private static final int resetPreferencesMenuItemId = 0;
	
	private Steer application;
	private SharedPreferences preferences;

    Toolbar mToolbar;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.settings);
        setTheme(R.style.Settings);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        mToolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_default, root, false);
        mToolbar.setTitle(R.string.text_settings);
        ListView list = (ListView) findViewById(android.R.id.list);
        this.application = (Steer) this.getApplication();
		this.preferences = this.application.getPreferences();
        root.addView(mToolbar, 0); // insert at top
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



	}

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        theme.applyStyle(resid, true);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);
        if (preference != null) {
            if (preference instanceof PreferenceScreen) {
                if (((PreferenceScreen) preference).getDialog() != null) {
                    ((PreferenceScreen) preference)
                            .getDialog()
                            .getWindow()
                            .getDecorView()
                            .setBackgroundDrawable(
                                    this
                                            .getWindow()
                                            .getDecorView()
                                            .getBackground()
                                            .getConstantState()
                                            .newDrawable()
                            );
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause()
	{
		super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
		this.checkPreferences();
		finish();

	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, resetPreferencesMenuItemId, Menu.NONE, this.getResources().getString(R.string.text_reset_preferences));
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case resetPreferencesMenuItemId:
				this.resetPreferences();
				break;
		}
		onBackPressed();
		return super.onOptionsItemSelected(item);
	}
	
	private void checkPreferences()
	{
		Editor editor = this.preferences.edit();
		
		for (String s : tabFloatPreferences)
		{
			try
			{
				Float.parseFloat(this.preferences.getString(s, null));
			}
			catch (NumberFormatException e)
			{
				this.application.debug(e);
				editor.remove(s);
			}
		}
		
		for (String s : tabIntPreferences)
		{
			try
			{
				Integer.parseInt(this.preferences.getString(s, null));
			}
			catch (NumberFormatException e)
			{
				this.application.debug(e);
				editor.remove(s);
			}
		}
		
		editor.commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.settings, true);
	}


	@SuppressWarnings("deprecation")
	private void resetPreferences()
	{
		this.setPreferenceScreen(null);
		
		Editor editor = this.preferences.edit();
		editor.clear();
		editor.commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.settings, true);
		
		this.addPreferencesFromResource(R.xml.settings);
	}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
