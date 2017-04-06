package com.example.kevin.pomodoro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kevin.pomodoro.R;
import com.example.kevin.pomodoro.R2;
import com.example.kevin.pomodoro.Settings;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsDialogFragment extends DialogFragment {

    public interface OnSettingsAccessedListener {
        void OnSettingsAccessed(Settings settings);
    }

    private final String TAG = SettingsDialogFragment.class.getSimpleName();

    @BindView(R2.id.workSeekBar)
    SeekBar workSeekBar;
    @BindView(R2.id.restSeekBar)
    SeekBar restSeekBar;
    @BindView(R2.id.workText)
    TextView workText;
    @BindView(R2.id.restText)
    TextView restText;
    @BindView(R2.id.defaultThemeButton)
    RadioButton defaultTheme;
    @BindView(R2.id.purpleThemeButton)
    RadioButton purpleTheme;
    @BindView(R2.id.blueThemeButton)
    RadioButton blueTheme;
    @BindView(R2.id.vibrateCheckBox)
    CheckBox vibratorCheckbox;

    private final int MAX_WORK = 60;
    private final int MIN_WORK = 5;
    private final int MAX_REST = 25;
    private final int MIN_REST = 1;

    private final String WORK_THEME = "work_theme";
    private final String WORK_THEME_BORDER = "work_theme_border";
    private final String WORK_TEXT_COLOR = "work_text_color";

    private Settings mSettings;
    private int mWorkTime;
    private int mRestTime;
    private int mWorkTheme;
    private Boolean mHasVibration;


    public static SettingsDialogFragment newInstance(String settings) { //From Gson, the json representation of our Settings object
        SettingsDialogFragment frag = new SettingsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Settings", settings);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.settings_popup, null);
        ButterKnife.bind(this, v);

        Gson gson = new Gson();
        String jsonsettings = getArguments().getString("Settings");
        mSettings = gson.fromJson(jsonsettings, Settings.class);

        mWorkTime = mSettings.getWorkTime();
        mRestTime = mSettings.getRestTime();
        mWorkTheme = mSettings.getTheme();
        mHasVibration = mSettings.hasVibration();

        //setup
        setSeekBars(mWorkTime, mRestTime); //seeks bar values to current user settings
        setThemeSetting(mWorkTheme); //current work theme is checked
        setVibrationSetting(mHasVibration); //current vibration choice checked/not checked

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }

    @OnClick(R2.id.applyButton)
    void onApply() {
        Map<String, Integer> map = getWorkThemeChoice();

        //update settings and pass it to main activity
        mSettings.updateSettings(
                mWorkTime,
                mRestTime,
                map.get(WORK_THEME),
                map.get(WORK_THEME_BORDER),
                map.get(WORK_TEXT_COLOR),
                vibratorCheckbox.isChecked()
        );

        ((OnSettingsAccessedListener) getActivity()).OnSettingsAccessed(mSettings);
        dismiss();
    }

    @OnClick(R2.id.resetSettings)
    void onReset() {
        // set all the settings to default
        setSeekBars(Settings.DEFAULT_WORK_TIME, Settings.DEFAULT_REST_TIME); // default times
        setThemeSetting(Settings.DEFAULT_THEME); // default theme
        setVibrationSetting(Settings.DEFAULT_VIBRATION); // default vibration
    }


    //get the work theme settings and the accompanying text color/border theme
    public Map<String, Integer> getWorkThemeChoice() {

        Map<String, Integer> workTheme = new HashMap<>();

        if (defaultTheme.isChecked()) {
            workTheme.put(WORK_THEME, R.drawable.theme_green_screen);
            workTheme.put(WORK_THEME_BORDER, R.drawable.theme_default_work_border);
            workTheme.put(WORK_TEXT_COLOR, R.color.defaultTimerTextColor);

        } else if (purpleTheme.isChecked()) {
            workTheme.put(WORK_THEME, R.drawable.theme_purple_screen);
            workTheme.put(WORK_THEME_BORDER, R.drawable.theme_purple_work_border);
            workTheme.put(WORK_TEXT_COLOR, R.color.purpleTimerThemeTextColor);

        } else if (blueTheme.isChecked()) {
            workTheme.put(WORK_THEME, R.drawable.theme_blue_screen);
            workTheme.put(WORK_THEME_BORDER, R.drawable.theme_blue_work_border);
            workTheme.put(WORK_TEXT_COLOR, R.color.blueTimerThemeTextColor);
        }

        return workTheme;
    }


    /**
     * The following set the settings menu with user's preconfigured settings from before
     */
    private void setVibrationSetting(Boolean currentVibration) {
        if (currentVibration) {
            vibratorCheckbox.setChecked(true);
        } else {
            vibratorCheckbox.setChecked(false);
        }
    }


    private void setThemeSetting(int theme) {

        switch (theme) {
            case R.drawable.theme_green_screen:
                defaultTheme.setChecked(true);
                break;

            case R.drawable.theme_purple_screen:
                purpleTheme.setChecked(true);
                break;

            case R.drawable.theme_blue_screen:
                blueTheme.setChecked(true);
                break;
        }

    }


    public void setSeekBars(final int currentWorkTime, final int currentRestTime) {
        workSeekBar.setMax(MAX_WORK - MIN_WORK);
        workSeekBar.setProgress(currentWorkTime - MIN_WORK);
        workText.setText(String.format(Locale.getDefault(), "Current: %d minutes", currentWorkTime));
        workSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mWorkTime = MIN_WORK + progress;
                workText.setText(String.format(Locale.getDefault(), "Current: %d minutes", mWorkTime));
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        restSeekBar.setMax(MAX_REST - MIN_REST);
        restSeekBar.setProgress(currentRestTime - MIN_REST);
        restText.setText(String.format(Locale.getDefault(), "Current: %d minutes", currentRestTime));
        restSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mRestTime = MIN_REST + progress;
                restText.setText(String.format(Locale.getDefault(), "Current: %d minutes", mRestTime));
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
