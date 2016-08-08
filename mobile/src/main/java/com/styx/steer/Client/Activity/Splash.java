package com.styx.steer.Client.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.styx.steer.Client.R;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent logo = new Intent(Splash.this, Home.class);
                    startActivity(logo);
                }
            }
        };
        timer.start();
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
