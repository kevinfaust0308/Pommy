/**
 * A screen to introduce users to the app
 * If it's the first time for user to open the app, they will be prompted for their name which will be saved onto the app's data
 * Otherwise, user will be redirected to main app screen
 */

package com.example.kevin.pomodoro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.kevin.pomodoro.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();
    private static final String PREFS_FILE = "com.example.kevin.pomodoro.preferences";

    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        boolean firstTime = mPreferences.getBoolean("FirstTimeUser", true);

        //if we already have a user, skip this page
        if (!firstTime) {
            launchMain(false); //don't show help popup box. not a new user
        }

    }


    @OnClick(R.id.rel)
    public void launchUserNameDialog() {
        mPreferences.edit().putBoolean("FirstTimeUser", false).apply();
        launchMain(true); //show help popup box. a new user
    }


    //launch Main Activity and display/hide help popup box
    private void launchMain(boolean showHelp) {
        Intent intent = new Intent(this, MainActivity.class);
        //clears the stack: if we click "back" button from MainActivity, the app closes. we remove this from the stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(getString(R.string.ShowHelpIfComingFromIntro), showHelp);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_animation_in, R.anim.fade_animation_out);
    }


}

