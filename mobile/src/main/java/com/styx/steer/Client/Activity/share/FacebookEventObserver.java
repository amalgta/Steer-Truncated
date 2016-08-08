package com.styx.steer.Client.Activity.share;

import android.app.Activity;
import android.content.SharedPreferences;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.common.LogoutListener;
import com.nostra13.socialsharing.common.PostListener;
import com.nostra13.socialsharing.facebook.FacebookEvents;
import com.styx.steer.Client.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class FacebookEventObserver {

    public static Boolean unlock = false;
    SharedPreferences preferences;
    private Reference<Activity> context;
    private AuthListener authListener = new AuthListener() {
        @Override
        public void onAuthSucceed() {
            showToastOnUIThread(R.string.toast_facebook_auth_success);
        }

        @Override
        public void onAuthFail(String error) {
            showToastOnUIThread(R.string.toast_facebook_auth_fail);
        }
    };
    private PostListener postListener = new PostListener() {
        @Override
        public void onPostPublishingFailed() {
            showToastOnUIThread(R.string.facebook_post_publishing_failed);
        }

        @Override
        public void onPostPublished() {
            unlock = true;
            showToastOnUIThread(R.string.facebook_post_published);
        }
    };
    private LogoutListener logoutListener = new LogoutListener() {
        @Override
        public void onLogoutComplete() {
            showToastOnUIThread(R.string.facebook_logged_out);
        }
    };

    private FacebookEventObserver() {
        context = new WeakReference<Activity>(null);
    }

    public static FacebookEventObserver newInstance() {
        return new FacebookEventObserver();
    }

    private void showToastOnUIThread(final int textRes) {
        final Activity curActivity = context.get();
        if (curActivity != null) {
            curActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackbarManager.show(
                            Snackbar.with(curActivity) // context
                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                    .text(textRes) // text to be displayed
                                    .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                            , curActivity);
//					Toast.makeText(curActivity, textRes, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Should be call at {@link android.app.Activity#onStart()}
     */
    public void registerListeners(Activity context) {
        this.context = new WeakReference<Activity>(context);

        FacebookEvents.addAuthListener(authListener);
        FacebookEvents.addPostListener(postListener);
        FacebookEvents.addLogoutListener(logoutListener);
    }

    /**
     * Should be call at {@link android.app.Activity#onStop()}
     */
    public void unregisterListeners() {
        context.clear();

        FacebookEvents.removeAuthListener(authListener);
        FacebookEvents.removePostListener(postListener);
        FacebookEvents.removeLogoutListener(logoutListener);
    }
}
