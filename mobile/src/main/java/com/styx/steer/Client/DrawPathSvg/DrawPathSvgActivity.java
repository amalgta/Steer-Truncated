package com.styx.steer.Client.DrawPathSvg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.styx.steer.Client.Activity.Splash;
import com.styx.steer.Client.R;

public class DrawPathSvgActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svg_activity);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = getLayoutInflater();
        addSvgView(inflater, container);
    }

    private void addSvgView(LayoutInflater inflater, LinearLayout container) {
        final View view = inflater.inflate(R.layout.item_svg, container, false);
        final SvgView svgView = (SvgView) view.findViewById(R.id.svg);
//      Replace your Logo
        svgView.setSvgResource(R.raw.logo);
        view.setBackgroundResource(R.color.myPrimaryDarkColor);
        svgView.setmCallback(new SvgCompletedCallBack() {

            @Override
            public void onSvgCompleted() {
                startActivity(new Intent(DrawPathSvgActivity.this, Splash.class));
            }
        });

        container.addView(view);

        Handler handlerDelay = new Handler();
        handlerDelay.postDelayed(new Runnable() {
            public void run() {
                svgView.startAnimation();
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}