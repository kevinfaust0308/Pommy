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

    private int mWorkTime;
    private int mRestTime;

    private int mWorkTheme;
    private int mWorkThemeBorder;
    private int mWorkTextColor;

    private boolean mHasVibration;


    public Settings() {
        mWorkTime = 25;
        mRestTime = 5;

        mWorkTheme = R.drawable.theme_green_screen;
        mWorkThemeBorder = R.drawable.theme_default_work_border;
        mWorkTextColor = R.color.defaultTimerTextColor;

        mHasVibration = true;
    }


    public void updateSettings(int workTime, int restTime, int workTheme, int workThemeBorder, int workTextColor, boolean hasVibration) {
        mWorkTime = workTime;
        mRestTime = restTime;

        mWorkTheme = workTheme;
        mWorkThemeBorder = workThemeBorder;
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


    public int getWorkTheme() {
        return mWorkTheme;
    }


    public void setWorkTheme(int workTheme) {
        mWorkTheme = workTheme;
    }


    public int getWorkThemeBorder() {
        return mWorkThemeBorder;
    }


    public void setWorkThemeBorder(int workThemeBorder) {
        mWorkThemeBorder = workThemeBorder;
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
