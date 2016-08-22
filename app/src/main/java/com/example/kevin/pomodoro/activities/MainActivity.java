package com.example.kevin.pomodoro.activities;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.pomodoro.Progress;
import com.example.kevin.pomodoro.R;
import com.example.kevin.pomodoro.Settings;
import com.example.kevin.pomodoro.Timer;
import com.example.kevin.pomodoro.User;
import com.example.kevin.pomodoro.enums.Mode;
import com.example.kevin.pomodoro.enums.State;
import com.example.kevin.pomodoro.fragments.HelpDialogFragment;
import com.example.kevin.pomodoro.fragments.SettingsDialogFragment;
import com.example.kevin.pomodoro.fragments.StatisticsDialogFragment;
import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Timer.TimerListener, SettingsDialogFragment.OnSettingsAccessedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // SHARED PREFERENCES
    private static final String PREFS_FILE = "com.example.kevin.pomodoro.preferences";
    public Settings mSettings;
    public User mUser;
    public Progress mProgress;
    @BindView(R.id.timerText)
    TextView timerText;
    @BindView(R.id.pauseButton)
    ImageButton pauseButton;
    @BindView(R.id.restartButton)
    ImageButton restartButton;
    @BindView(R.id.startButton)
    ImageButton startButton;
    @BindView(R.id.rel)
    RelativeLayout layout;
    @BindView(R.id.tomatoPicture)
    ImageView tomato;
    @BindView(R.id.helpButton)
    ImageButton helpButton;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    //////////////////////////////For the Navigation Drawer//////////////////////
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_drawer)
    NavigationView mNavView;
    //////////////////////////////GSON (object shared preferences)/////////////////////////////////
    Gson gson;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    private PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    private ActionBarDrawerToggle mDrawerToggle;
    private Timer mTimer;
    ///header
    private TextView headerUsernameText;
    private Menu mNavMenu;
    ///////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            float px = 16 * getResources().getDisplayMetrics().density; //16dp to px
            Log.d(TAG, "the px is " + Math.round(px));
            layout.setPadding(0, Math.round(px), 0, 16);
        }


        mPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        gson = new Gson();

        //get our json representations of our objects if exist. Else, create json representation of a brand new object
        String settings = mPreferences.getString("Settings", gson.toJson(new Settings()));
        String user = mPreferences.getString("User", ""); //User object must exist if we are at this page. ignore default value
        String progress = mPreferences.getString("Progress", gson.toJson(new Progress()));

        //convert json representations to actual objects
        mSettings = gson.fromJson(settings, Settings.class);
        mUser = gson.fromJson(user, User.class);
        mProgress = gson.fromJson(progress, Progress.class);

        // launches help screen when app is first launched and grab user name
        if (getIntent().getBooleanExtra(getString(R.string.ShowHelpIfComingFromIntro), false)) { //SHOWHELP will always be true/false. ignore default value
            launchHelpScreen();
        }

        //+1 for you :D
        mProgress.incrementAmountOfTimesOpened();

        //new timer with either saved or new settings
        mTimer = new Timer(mSettings.getWorkTime(), mSettings.getRestTime(), MainActivity.this);

        if (timerText == null) {
            Log.d(TAG, "timer text is null");
        } else {
            Log.d(TAG, "timer text is NOT null");
        }

        // -->change 25:00 to our settings
        updateTimerText(String.format(Locale.getDefault(), "%d:00", mTimer.getMinutesStart()));
        // -->change default green theme to pre-chosen theme
        toggleUI(Mode.REST);

        //navigation view setup + toolbar
        //setupNavView() can only be called once mUser is initialized because we call User.getName() in this function
        setSupportActionBar(toolbar);
        setupNavView();

        //listeners

        timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartButton.setVisibility(View.VISIBLE);
                /*
                --> The toggle functions change the "Mode" property of our timer
                --> We want to grab the "Mode" of our timer as of now and do stuff using that value
                 */
                Mode mode = mTimer.getMode();
                toggleUI(mode); //JUST changes ui depending on mode
                mTimer.changeModeAndStart(mode); //toggles mode and state
                toggleButtonVisibilities();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHelpScreen();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartButton.setVisibility(View.VISIBLE); //restart button is initially hidden
                mTimer.play();
                toggleButtonVisibilities();

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.pause();
                toggleButtonVisibilities();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.restart();
                toggleButtonVisibilities();
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * --> Activity overridden methods
     * --> interface callback implementations
     * <p>
     * OnTimerFinish:
     * Interface implementation. --> Timer.java
     * Called when timer reaches 0
     * <p>
     * OnTimerTick:
     * Interface implementation. --> Timer.java
     * Called every tick
     * <p>
     * OnSettingsAccessed:
     * Interface implementation. --> SettingsDialogFragment.java
     * Called when we come back to main activity from settings menu
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");

        //store the string representation of our objects using Gson
        mEditor.putString("Settings", gson.toJson(mSettings));
        mEditor.putString("Progress", gson.toJson(mProgress));
        //user object saved in LauncherActivity
        mEditor.apply();
    }


    public void toggleButtonVisibilities() {

        if (mTimer.getState() == State.PAUSED) {
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        } else {
            pauseButton.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
    }


    /*
    Popup dialogs
     */
    public void launchHelpScreen() {
        DialogFragment fragment = new HelpDialogFragment();
        fragment.show(getFragmentManager(), "HelpDialog");
    }


    public void launchStatScreen() {
        StatisticsDialogFragment dia = new StatisticsDialogFragment().newInstance(gson.toJson(mProgress));
        dia.show(getFragmentManager(), "ProgressDialog");
    }


    public void launchSettingsScreen() {
        //SettingsDialogFragment dia = new SettingsDialogFragment().newInstance(gson.toJson(mSettings));
        SettingsDialogFragment dia = new SettingsDialogFragment().newInstance(gson.toJson(mSettings));
        dia.show(getFragmentManager(), "SettingsDialog");
    }


    public void toggleUI(Mode mode) {
        //change the mode of the timer and update UI accordingly
        if (mode == Mode.WORK) {
            //none of this is changeable by user
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.theme_red_screen)); //theme
            timerText.setBackground(ContextCompat.getDrawable(this, R.drawable.theme_default_rest_border)); //border
            timerText.setTextColor(ContextCompat.getColor(this, R.color.defaultTimerTextColor)); //timer text color
            tomato.setImageResource(R.drawable.blacktomato);
        }
        if (mode == Mode.REST) {
            layout.setBackground(ContextCompat.getDrawable(this, mSettings.getWorkTheme())); //theme
            timerText.setBackground(ContextCompat.getDrawable(this, mSettings.getWorkThemeBorder())); //border
            timerText.setTextColor(ContextCompat.getColor(this, mSettings.getWorkTextColor())); //timer text color
            tomato.setImageResource(R.drawable.redtomato);
        }
    }


    public void updateTimerText(int minutes, int seconds) {
        timerText.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
        /*can do this with decimal format also
        NumberFormat f = new DecimalFormat("00");
        timerText.setText(minutes + ":" + f.format(seconds)); // formatter used here*/
    }


    public void updateTimerText(String time) {
        timerText.setText(time);
    }


    @Override
    public void OnTimerFinish() {
        if (mSettings.hasVibration()) vibrate(2000);

        Mode mode = mTimer.getMode();

        if (mode.equals(Mode.WORK) && mTimer.getMinutesStart() > 25 * 60 * 1000)
            mProgress.incrementCompletedWorkRounds();
        else if (mode.equals(Mode.REST) && mTimer.getMinutesStart() < 10 * 60 * 1000)
            mProgress.incrementCompletedRestRounds();

        toggleUI(mode);
        mTimer.changeModeAndStart(mode);
    }


    @Override
    public void OnTimerTick(int minutes, int seconds) {
        updateTimerText(minutes, seconds);
    }


    public void vibrate(int duration) {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(duration);
    }


    @Override
    public void OnSettingsAccessed(Settings settings) {
        mSettings = settings;

        mTimer.pause();

        mTimer.updateTimerWithNewSettings(mSettings.getWorkTime(), mSettings.getRestTime());

        toggleUI(Mode.REST);//this will give us the work UI
        updateTimerText(String.format(Locale.getDefault(), "%d:00", mTimer.getMinutesStart())); //updates timer text with new settings
        restartButton.setVisibility(View.GONE);
        toggleButtonVisibilities();
    }


    /**
     * Navigation View
     * --> name of user is displayed
     * --> Home, Statistics, and Settings options
     */
    private void setupNavView() {
        mNavView.setNavigationItemSelectedListener(this); // "this" because our navigation select listener is this activity (below) (implemented)

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ///HEADER VIEW

        View v = mNavView.getHeaderView(0); ////////////////////////////////////////// <<<----this thing is duh best
        headerUsernameText = (TextView) v.findViewById(R.id.nameOfCurrentUser);
        Log.d(TAG, "Setting user's name: " + mUser.getName());
        headerUsernameText.setText(mUser.getName());

        /*
        //SOLELY FOR FONT USE. DISREGARD
        headerUsernameText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.ArchitectFont)));
        TextView t = (TextView) v.findViewById(R.id.welcome);
        t.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.ArchitectFont)));
        */
        ///

        mNavMenu = mNavView.getMenu(); //get the menu from nav

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //nav drawer items on click

        int current = item.getItemId();

        switch (current) {
            case R.id.home:
                mDrawerLayout.closeDrawer(GravityCompat.START); //closes the drawer by setting the gravity to "start" (all the way to the left hidden)
                break;
            case R.id.statistics:
                layout.getBackground().setColorFilter(filter);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                launchStatScreen();
                layout.getBackground().setColorFilter(null);
                break;
            case R.id.settings:
                layout.getBackground().setColorFilter(filter);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                launchSettingsScreen();
                layout.getBackground().setColorFilter(null);
                break;
        }
        return false;
    }
}

