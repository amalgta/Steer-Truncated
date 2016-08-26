package com.styx.steer.Client.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;
import com.styx.steer.Client.View.CircleImageView;
import com.styx.steer.Client.View.CircleLayout;
import com.styx.steer.Client.View.ControlView;
import com.styx.steer.Protocol.SteerActionReceiver;
import com.styx.steer.Protocol.action.Combination;
import com.styx.steer.Protocol.action.KeyboardAction;
import com.styx.steer.Protocol.action.ScreenCaptureResponseAction;
import com.styx.steer.Protocol.action.SteerAction;



public class Browser extends MainActivity implements SteerActionReceiver, CircleLayout.OnItemSelectedListener,CircleLayout.OnItemClickListener, CircleLayout.OnRotationFinishedListener, CircleLayout.OnCenterClickListener {
    TextView selectedTextView;
    boolean UnlockedForeverb;
    private Steer application;
    private ControlView controlView;
    private SharedPreferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.application = (Steer) this.getApplication();
        this.preferences = this.application.getPreferences();


        Rotatorui();


        mTitle.setText("Browser");
        startIntroAnimation();

    }



    public void Rotatorui()
    {
        String rotator = preferences.getString("menus", "");
        if (rotator.equals("Circular")){
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.browserr, null, false);
            mDrawer.addView(contentView, 0);

            CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
            circleMenu.setOnItemSelectedListener(this);
            circleMenu.setOnItemClickListener(this);
            circleMenu.setOnRotationFinishedListener(this);
            circleMenu.setOnCenterClickListener(this);
            selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "times.ttf");
            selectedTextView.setTypeface(tf);
            selectedTextView.setText(((CircleImageView) circleMenu
                    .getSelectedItem()).getName());



        }
        else
        {
            FrameLayout containerView = (FrameLayout) findViewById(R.id.container);
            int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
            int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
            int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
            int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
            int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
            int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
            int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

            ImageView fabIconStar = new ImageView(this);
            fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.home_browser));

            FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            starParams.setMargins(redActionButtonMargin, redActionButtonMargin, redActionButtonMargin, redActionButtonMargin);
            fabIconStar.setLayoutParams(starParams);

            FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
            fabIconStarParams.setMargins(redActionButtonContentMargin, redActionButtonContentMargin, redActionButtonContentMargin, redActionButtonContentMargin);

            FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this).setContentView(fabIconStar, fabIconStarParams).setPosition(FloatingActionButton.POSITION_TOP_CENTER).setLayoutParams(starParams).setContentView(containerView).build();
            leftCenterButton.removeView(fabIconStar);
            leftCenterButton.addView(fabIconStar, 0);
            // Set up customized SubActionButtons for the right center menu
            SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
//            lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

            FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            blueContentParams.setMargins(blueSubActionButtonContentMargin, blueSubActionButtonContentMargin, blueSubActionButtonContentMargin, blueSubActionButtonContentMargin);
            lCSubBuilder.setLayoutParams(blueContentParams);
            // Set custom layout params
            FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            lCSubBuilder.setLayoutParams(blueParams);

            ImageView lcIcon1 = new ImageView(this);
            ImageView lcIcon2 = new ImageView(this);
            ImageView lcIcon3 = new ImageView(this);
            ImageView lcIcon4 = new ImageView(this);
            ImageView lcIcon5 = new ImageView(this);
            ImageView lcIcon6 = new ImageView(this);
            ImageView lcIcon7 = new ImageView(this);
            ImageView lcIcon8 = new ImageView(this);
            ImageView lcIcon9 = new ImageView(this);

            lcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.browser_bookmark));
            lcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.browser_download));
            lcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.browser_exit));
            lcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.browser_history));
            lcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.browser_refresh));
            lcIcon6.setImageDrawable(getResources().getDrawable(R.drawable.browser_newtab));
            lcIcon7.setImageDrawable(getResources().getDrawable(R.drawable.browser_switchtab));
            lcIcon8.setImageDrawable(getResources().getDrawable(R.drawable.browser_save));
            lcIcon9.setImageDrawable(getResources().getDrawable(R.drawable.browser_print));

            FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(this).addSubActionView(lCSubBuilder.setContentView(lcIcon1, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon2, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon3, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon4, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon5, blueContentParams).build())
                    .addSubActionView(lCSubBuilder.setContentView(lcIcon6, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon7, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon8, blueContentParams).build()).addSubActionView(lCSubBuilder.setContentView(lcIcon9, blueContentParams).build()).setRadius(redActionMenuRadius).setStartAngle(0).setEndAngle(360).attachTo(leftCenterButton).build();

//      For Adview
            AdView adView = new AdView(this);

                adView.setVisibility(View.GONE);


//      For position
            FloatingActionButton.LayoutParams adParams = new FloatingActionButton.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            adParams.gravity = Gravity.BOTTOM;
            adView.setLayoutParams(adParams);
            containerView.addView(adView);


            //          Doing Onclick for the FAB !
            lcIcon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(45));
                }
            });

            lcIcon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(42));
                }
            });

            lcIcon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(50));
                }
            });

            lcIcon4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(41));
                }
            });

            lcIcon5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new KeyboardAction(25));
                }
            });

            lcIcon6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(43));
                }
            });

            lcIcon7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(44));
                }
            });

            lcIcon8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(46));
                }
            });

            lcIcon9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.sendAction(new Combination(47));
                }
            });

        }
    }

    @Override
    public void onItemSelected(View view, String name) {
        selectedTextView.setText(name);
    }

    @Override
    public void onItemClick(View view, String name) {
        switch (view.getId())
        {
					case R.id.Bookmark:
						application.sendAction(new Combination(45));
						break;
					case R.id.Download:
						application.sendAction(new Combination(42));
						break;
					case R.id.Exit:
						application.sendAction(new Combination(50));
						break;
					case R.id.History:
						application.sendAction(new Combination(41));
						break;
					case R.id.Refresh:
						application.sendAction(new KeyboardAction(25));
						break;
					case R.id.New_Tab:
						application.sendAction(new Combination(43));
						break;
					case R.id.Next_Tab:
						application.sendAction(new Combination(44));
						break;
					case R.id.Save:
						application.sendAction(new Combination(46));
						break;
                    case R.id.Print:
                        application.sendAction(new Combination(47));
                        break;
				}
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

	@Override
	protected void onResume()
	{
		super.onResume();
		this.application.registerActionReceiver(this);
	}
	
	protected void onPause()
	{
		super.onPause();
		this.application.unregisterActionReceiver(this);
		finish();
	}
	
	public void receiveAction(SteerAction action)
	{
		if (action instanceof ScreenCaptureResponseAction)
		{
			this.controlView.receiveAction((ScreenCaptureResponseAction) action);
		}
	}
	
}