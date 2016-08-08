package com.styx.steer.Client.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.styx.steer.Client.Activity.connection.ConnectionListActivity;
import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;
import com.styx.steer.Client.View.CircleImageView;
import com.styx.steer.Client.View.CircleLayout;


public class Home extends MainActivity implements CircleLayout.OnItemClickListener, CircleLayout.OnItemSelectedListener, CircleLayout.OnRotationFinishedListener, CircleLayout.OnCenterClickListener {
    public String rotator;
    TextView selectedTextView;
    TextView mTitle;
    private Steer application;
    private SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (Steer) this.getApplication();
        this.preferences = this.application.getPreferences();
        rotator = preferences.getString("menus", "");
        this.checkOnCreate();
        mTitle= (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "times.ttf");
        mTitle.setTypeface(tf);
        mTitle.setText("Steer");
        startIntroAnimation();
        Rotatorui();
    }

    public void Rotatorui() {
        String rotator = preferences.getString("menus", "");
        if (rotator.equals("Circular")) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.home, null, false);
            mDrawer.addView(contentView, 0);

            CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
            circleMenu.setOnItemClickListener(this);
            circleMenu.setOnItemSelectedListener(this);
            circleMenu.setOnRotationFinishedListener(this);
            circleMenu.setOnCenterClickListener(this);
            selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "times.ttf");
            selectedTextView.setTypeface(tf);
            selectedTextView.setText(((CircleImageView) circleMenu
                    .getSelectedItem()).getName());



        } else {
            FrameLayout containerView = (FrameLayout) findViewById(R.id.container);
            int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
            int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
            int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
            int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
            int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
            int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
            int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

            ImageView fabIconStar = new ImageView(this);
            fabIconStar.setImageDrawable(getResources().getDrawable(R.mipmap.app_icon));

            FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            starParams.setMargins(redActionButtonMargin, redActionButtonMargin, redActionButtonMargin, redActionButtonMargin);
            fabIconStar.setLayoutParams(starParams);

            FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
            fabIconStarParams.setMargins(redActionButtonContentMargin, redActionButtonContentMargin, redActionButtonContentMargin, redActionButtonContentMargin);

            FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this).setContentView(fabIconStar, fabIconStarParams).setPosition(FloatingActionButton.POSITION_TOP_CENTER).setLayoutParams(starParams).setContainerView(containerView).build();
            leftCenterButton.removeView(fabIconStar);
            leftCenterButton.addView(fabIconStar, 0);
            // Set up customized SubActionButtons for the right center menu
            SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);

            FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            blueContentParams.setMargins(blueSubActionButtonContentMargin, blueSubActionButtonContentMargin, blueSubActionButtonContentMargin, blueSubActionButtonContentMargin);
            lCSubBuilder.setLayoutParams(blueContentParams);
            // Set custom layout params

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setGravity(Gravity.BOTTOM);
            leftCenterButton.addView(layout);


            FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            lCSubBuilder.setLayoutParams(blueParams);

            ImageView lcIcon1 = new ImageView(this);
            ImageView lcIcon2 = new ImageView(this);
            ImageView lcIcon3 = new ImageView(this);
            ImageView lcIcon4 = new ImageView(this);
            ImageView lcIcon5 = new ImageView(this);
            //ImageView lcIcon6 = new ImageView(this);
            // ImageView lcIcon7 = new ImageView(this);
            // ImageView lcIcon8 = new ImageView(this);

            lcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.home_connections));
            lcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.home_mouse));
            lcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.home_mirror));
            lcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.home_file));
            lcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.home_short));


            ///  lcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.home_file));
            //lcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.home_browser));
            // lcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.home_short));
            // lcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.home_ppt));
            //lcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.home_media));
            //   lcIcon6.setImageDrawable(getResources().getDrawable(R.drawable.home_connections));
            //  lcIcon7.setImageDrawable(getResources().getDrawable(R.drawable.home_mouse));
            // lcIcon8.setImageDrawable(getResources().getDrawable(R.drawable.home_help));

            // Build another menu with custom options
            FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(this).addSubActionView(lCSubBuilder.setContentView(lcIcon1, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon2, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon3, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon4, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon5, blueContentParams).build()).setRadius(redActionMenuRadius).setStartAngle(0).setEndAngle(360).attachTo(leftCenterButton).build();



            //          Doing Onclick for the FAB !
            lcIcon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent e = new Intent(Home.this, ConnectionListActivity.class);
                    startActivity(e);
                }
            });

            lcIcon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent g = new Intent(Home.this, ControlActivity.class);
                    startActivity(g);
                }
            });

            lcIcon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ac = new Intent(Home.this, ControlActivity.class);
                    ac.putExtra("mirror", true);
                    startActivity(ac);
                }
            });

            lcIcon4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent c = new Intent(Home.this, FileExplorerActivity.class);
                    startActivity(c);
                }
            });

            lcIcon5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent b = new Intent(Home.this, EasyMode.class);
                    startActivity(b);
                }
            });


        }
    }

    @Override
    public void onItemClick(View view, String name) {
        switch (view.getId())
        {
					case R.id.connections:
						Intent ac = new Intent(Home.this, ConnectionListActivity.class);
						startActivity(ac);
						break;
					case R.id.mouse:
						Intent b = new Intent(Home.this, ControlActivity.class);
						startActivity(b);
						break;
            case R.id.mirror:
                Intent c = new Intent(Home.this, ControlActivity.class);
                c.putExtra("mirror", true);
                startActivity(c);
                // this.toggleKeyboard();
                break;
            case R.id.file_explorer:
                Intent d = new Intent(Home.this, FileExplorerActivity.class);
                startActivity(d);
                break;
            case R.id.easymode:
                Intent e = new Intent(Home.this, EasyMode.class);
                startActivity(e);
                break;

        }
    }

    @Override
    public void onItemSelected(View view, String name) {
        selectedTextView.setText(name);
    }

    @Override
    public void onCenterClick() {
        Toast.makeText(getApplicationContext(), R.string.app_name,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRotationFinished(View view, String name) {
        Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2,
                view.getHeight() / 2);
        animation.setDuration(250);
        view.startAnimation(animation);
    }

    private void checkOnCreate()
    {
        if (this.checkFirstRun())
        {
            this.firstRunDialog();
        }
    }

    private boolean checkFirstRun()
    {
        return this.preferences.getBoolean("debug_firstRun", true);
    }

    private void firstRunDialog()
    {
        Home.this.startActivity(new Intent(Home.this, Tutorial.class));
        Home.this.disableFirstRun();
    }

    private void disableFirstRun()
    {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean("debug_firstRun", false);
        editor.commit();
    }


}