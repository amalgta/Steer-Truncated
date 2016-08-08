package com.styx.steer.Client.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;
import com.styx.steer.Client.View.ControlView;
import com.styx.steer.Protocol.SteerActionReceiver;
import com.styx.steer.Protocol.action.KeyboardAction;
import com.styx.steer.Protocol.action.MouseClickAction;
import com.styx.steer.Protocol.action.MouseMoveAction;
import com.styx.steer.Protocol.action.MouseWheelAction;
import com.styx.steer.Protocol.action.ScreenCaptureResponseAction;
import com.styx.steer.Protocol.action.SteerAction;

public class ControlActivity extends MainActivity implements SteerActionReceiver
{
	/*
	 * private static final int FILE_EXPLORER_MENU_ITEM_ID = 0; private static
	 * final int KEYBOARD_MENU_ITEM_ID = 1; private static final int
	 * CONNECTIONS_MENU_ITEM_ID = 2; private static final int
	 * SETTINGS_MENU_ITEM_ID = 3; private static final int HELP_MENU_ITEM_ID =
	 * 4;
	 */
    Boolean mode;//Mirror Mode?
    private Steer application;
	private SharedPreferences preferences;
	
	private ControlView controlView;
	private boolean debugging;
	
	private MediaPlayer mpClickOn;
	private MediaPlayer mpClickOff;
	
	private boolean feedbackSound;
	
	protected void onCreate(Bundle savedInstanceState)
	{
//        request();
        super.onCreate(savedInstanceState);
		this.application = (Steer) this.getApplication();
		this.preferences = this.application.getPreferences();
//        this.checkFullscreen();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.control, null, false);
        mode = getIntent().getBooleanExtra("mirror", false);
        Log.e("GTA_Control: Mirror", mode.toString());
        if (mode == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
        mDrawer.addView(contentView, 0);
        if (mode == true) {
            mTitle.setText("Mirror");
        } else {
            mTitle.setText("Mouse");

        }

        startIntroAnimation();
		this.setButtonsSize();

		this.controlView = (ControlView) this.findViewById(R.id.controlView);
		
		this.mpClickOn = MediaPlayer.create(this, R.raw.clickon);
		this.mpClickOff = MediaPlayer.create(this, R.raw.clickoff);

        this.debugging = this.preferences.getBoolean("debug_enabled", true);
    }

    protected void onResume()
    {
        super.onResume();

        this.application.registerActionReceiver(this);

        this.feedbackSound = this.preferences.getBoolean("feedback_sound", false);

        // This probably won't get called since the editText is set to MultiLine
        ((android.widget.EditText) findViewById(R.id.textline)).setOnEditorActionListener(new OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean handled = false;
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND)
                {
                    sendMessage(v.getText().toString());
                    v.setText("");
                    handled = true;
                }
                return handled;
            }

        });
        findViewById(R.id.inputSend).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                sendMessage(((android.widget.EditText) findViewById(R.id.textline)).getText().toString());
                ((android.widget.EditText) findViewById(R.id.textline)).setText("");
            }
        });
        findViewById(R.id.inputBackspace).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                application.sendAction(new KeyboardAction(-1));
            }
        });

    }

    protected void sendMessage(String s)
    {

        if (debugging)
            android.util.Log.d("Note", "Sending string: " + s);
        for (int i = 0; i < s.length(); i++)
            this.application.sendAction(new KeyboardAction(s.charAt(i)));
    }

    protected void onPause()
    {
        super.onPause();
        this.application.unregisterActionReceiver(this);
        finish();
    }

    public boolean onKeyUP(int KeyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
        {
            return true;
        }
        return event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        int unicode = event.getUnicodeChar();

        if (debugging)
            android.util.Log.d("Note", "Key Captured with keycode [" + keyCode + "] and unicode [" + unicode + "]");
        if (unicode == 0)
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_DEL:
                    unicode = KeyboardAction.UNICODE_BACKSPACE;
                    break;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    unicode = KeyboardAction.UNICODE_PAGEUP;
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    unicode = KeyboardAction.UNICODE_PAGEDN;
                    break;
                case KeyEvent.KEYCODE_TAB:
                    unicode = KeyboardAction.UNICODE_TAB;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    unicode = KeyboardAction.UNICODE_ARROW_UP;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    unicode = KeyboardAction.UNICODE_ARROW_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    unicode = KeyboardAction.UNICODE_ARROW_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    unicode = KeyboardAction.UNICODE_ARROW_RIGHT;
                    break;

            }
        if (unicode != 0)
        {
            this.application.sendAction(new KeyboardAction(unicode));
        }
        return super.onKeyDown(keyCode, event);
    }

    public void keyboard(View v){
        this.toggleKeyboard();
    }

    public void receiveAction(SteerAction action)
    {
        if ((action instanceof ScreenCaptureResponseAction) && (mode))
        {
            this.controlView.receiveAction((ScreenCaptureResponseAction) action);
        }
    }

    public void mouseClick(byte button, boolean state)
    {
        this.application.sendAction(new MouseClickAction(button, state));

        if (this.feedbackSound)
        {
            if (state)
            {
                this.playSound(this.mpClickOn);
            }
            else
            {
                this.playSound(this.mpClickOff);
            }
        }
    }

    public void mouseMove(int moveX, int moveY)
    {
        this.application.sendAction(new MouseMoveAction((short) moveX, (short) moveY));
    }

    public void mouseWheel(int amount)
    {
        this.application.sendAction(new MouseWheelAction((byte) amount));
    }

    private void playSound(MediaPlayer mp)
    {
        if (mp != null)
        {
            mp.seekTo(0);
            mp.start();
        }
    }

    private void toggleKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);

    }

    private void setButtonsSize()
    {
        LinearLayout clickLayout = (LinearLayout) this.findViewById(R.id.clickLayout);
        LinearLayout inputLayout = (LinearLayout) this.findViewById(R.id.inputLayout);

        int orientation = this.getResources().getConfiguration().orientation;

        int size = (int) (Float.parseFloat(this.preferences.getString("buttons_size", null)) * this.getResources().getDisplayMetrics().density);

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            clickLayout.getLayoutParams().height = size;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
                inputLayout.setTranslationY(size);
            else
            {
                // Fix for devices pre-3.0
                inputLayout.setPadding(0, size, 0, 0);
                inputLayout.getLayoutParams().height += size;

            }
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            clickLayout.getLayoutParams().width = size;
            inputLayout.setPadding(size, 0, 0, 0);
        }
    }
}
