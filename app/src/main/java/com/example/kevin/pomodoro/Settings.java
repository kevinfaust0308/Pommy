package com.example.kevin.pomodoro;

/**
 * Created by Kevin on 2016-05-25.
 * <p/>
 * -------- As of now ---------
 * Things can change:
 * -Work theme background color (by user)
 * -Work theme border (depending on user theme choice)
 * -Work theme text color (depending on user theme choice)
 * <p/>
 * Can't change anything to do with rest because user should always easily tell if they are on
 * rest mode if screen is red.
 */
public class Settings {

    public static final int DEFAULT_WORK_TIME = 25;
    public static final int DEFAULT_REST_TIME = 5;
    public static final int DEFAULT_THEME = R.drawable.theme_green_screen;
    public static final boolean DEFAULT_VIBRATION = true;

    private int mWorkTime;
    private int mRestTime;

    private int mTheme;
    private int mThemeBorder;
    private int mWorkTextColor;

    private boolean mHasVibration;


    public Settings() {
        mWorkTime = DEFAULT_WORK_TIME;
        mRestTime = DEFAULT_REST_TIME;

        mTheme = DEFAULT_THEME;
        mThemeBorder = R.drawable.theme_default_work_border;
        mWorkTextColor = R.color.defaultTimerTextColor;

        mHasVibration = DEFAULT_VIBRATION;
    }


    public void updateSettings(int workTime, int restTime, int workTheme, int workThemeBorder, int workTextColor, boolean hasVibration) {
        mWorkTime = workTime;
        mRestTime = restTime;

        mTheme = workTheme;
        mThemeBorder = workThemeBorder;
        mWorkTextColor = workTextColor;

        mHasVibration = hasVibration;
    }


    public int getWorkTime() {
        return mWorkTime;
    }


    public void setWorkTime(int workTime) {
        mWorkTime = workTime;
    }


    public int getRestTime() {
        return mRestTime;
    }


    public void setRestTime(int restTime) {
        mRestTime = restTime;
    }


    public int getTheme() {
        return mTheme;
    }


    public void setTheme(int theme) {
        mTheme = theme;
    }


    public int getThemeBorder() {
        return mThemeBorder;
    }


    public void setThemeBorder(int themeBorder) {
        mThemeBorder = themeBorder;
    }


    public int getWorkTextColor() {
        return mWorkTextColor;
    }


    public void setWorkTextColor(int workTextColor) {
        mWorkTextColor = workTextColor;
    }


    public boolean hasVibration() {
        return mHasVibration;
    }


    public void setVibration(boolean hasVibration) {
        mHasVibration = hasVibration;
    }
}
