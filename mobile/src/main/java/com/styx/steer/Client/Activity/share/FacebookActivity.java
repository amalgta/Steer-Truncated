package com.styx.steer.Client.Activity.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.facebook.FacebookFacade;
import com.styx.steer.Client.Activity.share.Constants.Extra;
import com.styx.steer.Client.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FacebookActivity extends Activity {

    public static Boolean unlock = false;
    private FacebookFacade facebook;
    private FacebookEventObserver facebookEventObserver;
    private TextView messageView;

    private String link;
    private String linkName;
    private String linkDescription;
    private String picture;
    private Map<String, String> actionsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_facebook);

        facebook = new FacebookFacade(this, Constants.FACEBOOK_APP_ID);
        facebookEventObserver = FacebookEventObserver.newInstance();

        messageView = (TextView) findViewById(R.id.message);
        TextView linkNameView = (TextView) findViewById(R.id.link_name);
        TextView linkDescriptionView = (TextView) findViewById(R.id.link_description);
//        Button postButton = (Button) findViewById(R.id.button_post);
        Button postImageButton = (Button) findViewById(R.id.button_post_image);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString(Extra.POST_MESSAGE);
            link = bundle.getString(Extra.POST_LINK);
            linkName = bundle.getString(Extra.POST_LINK_NAME);
            linkDescription = bundle.getString(Extra.POST_LINK_DESCRIPTION);
            picture = bundle.getString(Extra.POST_PICTURE);
            actionsMap = new HashMap<String, String>();
            actionsMap.put(Constants.FACEBOOK_SHARE_ACTION_NAME, Constants.FACEBOOK_SHARE_ACTION_LINK);

            messageView.setText(message);
            linkNameView.setText(linkName);
            linkDescriptionView.setText(linkDescription);
        }

//        postButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (facebook.isAuthorized()) {
//                    publishMessage();
//                    unlock = true;
//                    finish();
//                } else {
//                    // Start authentication dialog and publish message after successful authentication
//                    facebook.authorize(new AuthListener() {
//                        @Override
//                        public void onAuthSucceed() {
//                            publishMessage();
//                            unlock = true;
//                            showToastOnUIThread(R.string.facebook_post_published);
//                            finish();
//                        }
//
//                        @Override
//                        public void onAuthFail(String error) { // Do noting
//                            showToastOnUIThread(R.string.facebook_post_publishing_failed);
//                        }
//                    });
//                }
//            }
//        });
        postImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebook.isAuthorized()) {
                    publishImage();
                    unlock = true;
                    showToastOnUIThread(R.string.facebook_post_published);
                    finish();
                } else {
                    // Start authentication dialog and publish image after successful authentication
                    facebook.authorize(new AuthListener() {
                        @Override
                        public void onAuthSucceed() {
                            publishImage();
                            unlock = true;
                            showToastOnUIThread(R.string.facebook_post_published);
                            finish();
                        }

                        @Override
                        public void onAuthFail(String error) { // Do noting
                            showToastOnUIThread(R.string.facebook_post_publishing_failed);
                        }
                    });
                }
            }
        });
    }

    private void showToastOnUIThread(final int textRes) {
        SnackbarManager.show(
                Snackbar.with(this) // context
                        .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                        .text(textRes) // text to be displayed
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                , this);
    }

    private void publishMessage() {
        facebook.publishMessage(messageView.getText().toString(), link, linkName, linkDescription, picture, actionsMap);
    }

    private void publishImage() {
        Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.mipmap.app_icon)).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        facebook.publishImage(bitmapdata, Constants.FACEBOOK_SHARE_IMAGE_CAPTION);
    }

    @Override
    public void onStart() {
        super.onStart();
        facebookEventObserver.registerListeners(this);
        if (!facebook.isAuthorized()) {
            facebook.authorize();
        }
    }

    @Override
    public void onStop() {
        facebookEventObserver.unregisterListeners();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_facebook_twitter, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout:
                facebook.logout();
                return true;
            default:
                return false;
        }
    }
}
