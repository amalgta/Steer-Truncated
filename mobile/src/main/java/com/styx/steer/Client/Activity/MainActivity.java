package com.styx.steer.Client.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.styx.steer.Client.Activity.connection.ConnectionListActivity;
import com.styx.steer.Client.Activity.drawer.NsMenuAdapter;
import com.styx.steer.Client.Activity.drawer.NsMenuItemModel;
import com.styx.steer.Client.R;


public abstract class MainActivity extends AppCompatActivity
{
    private static final int ANIM_DURATION_TEXT = 400;
    private static final int ANIM_DURATION_TOOLBAR = 200;
    public TextView mTitle;
    protected ListView mDrawerList;
    protected DrawerLayout mDrawer;
    View view;
    String LOG_TAG = "steer";
    Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;
    private String[] menuItems;
    private String[] menuItemss;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mTitle= (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "times.ttf");
        mTitle.setTypeface(tf);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // set a custom shadow that overlays the main content when the drawer
        // opens
//		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.ns_menu_open, R.string.ns_menu_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerToggle.syncState();
        _initMenu();

    }

    public void startIntroAnimation() {
        int actionbarSize = 112;
        getToolbar().setTranslationY(-actionbarSize);
        getToolTitle().setTranslationY(-actionbarSize);
        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(100);
        getToolTitle().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TEXT)
                .setStartDelay(400)
                .start();
    }

    public void HideToolbar() {
        int actionbarSize = 200;
        getToolbar().setTranslationY(actionbarSize);
        getToolTitle().setTranslationY(actionbarSize);
        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(100);
        getToolTitle().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TEXT)
                .setStartDelay(400)
                .start();
    }
    private void _initMenu()
    {
        NsMenuAdapter mAdapter = new NsMenuAdapter(this);
        // Add Header
        //mAdapter.addHeader(R.string.ns_menu_main_header);

        // Add first block
        menuItems = getResources().getStringArray(R.array.ns_menu_items);
        String[] menuItemsIcon = getResources().getStringArray(R.array.ns_menu_items_icon);
        String[] menuItemscolor = getResources().getStringArray(R.array.ns_menu_items_color);

        int res = 0;
        int resc = 0;
        for (String item : menuItems)
        {

            int id_title = getResources().getIdentifier(item, "string", this.getPackageName());
            int id_icon = getResources().getIdentifier(menuItemsIcon[res], "drawable", this.getPackageName());
            int id_color = getResources().getIdentifier(menuItemscolor[resc], "drawable", this.getPackageName());

            NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon, id_color);
            // if (res==1) mItem.counter=12; //it is just an example...
            // if (res==3) mItem.counter=3; //it is just an example...
            mAdapter.addItem(mItem);
            res++;
            resc++;
        }
        mDrawerList = (ListView) findViewById(R.id.drawer);
        mDrawerList.setSelector(R.drawable.list_selector);
        if (mDrawerList != null)
            mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
    public TextView getToolTitle() {
        return mTitle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // Highlight the selected item, update the title, and close the
            // drawer
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            view.setSelected(true);
//            view.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
            mDrawer.closeDrawer(mDrawerList);

            switch (position)
            {
                case 0:
                    Intent a = new Intent(MainActivity.this, Home.class);
                    startActivity(a);
                    break;
                case 1:
                    Intent ac = new Intent(MainActivity.this, ConnectionListActivity.class);
                    startActivity(ac);
                    break;
                case 2:
                    Intent b = new Intent(MainActivity.this, ControlActivity.class);
                    startActivity(b);
                    break;
                case 3:
                    Intent c = new Intent(MainActivity.this, ControlActivity.class);
                    c.putExtra("mirror", true);
                    startActivity(c);
                    // this.toggleKeyboard();
                    break;
                case 4:
                    Intent d = new Intent(MainActivity.this, FileExplorerActivity.class);
                    startActivity(d);
                    break;
                case 5:
                    Intent e = new Intent(MainActivity.this, EasyMode.class);
                    startActivity(e);
                    break;
                default:
            }
        }
    }
}

