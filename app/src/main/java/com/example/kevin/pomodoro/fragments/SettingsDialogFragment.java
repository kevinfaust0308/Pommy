package com.example.kevin.pomodoro.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kevin.pomodoro.R;
import com.example.kevin.pomodoro.Settings;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingsDialogFragment extends DialogFragment {

    public interface OnSettingsAccessedListener {
        void OnSettingsAccessed(Settings settings);
    }

    private final String TAG = SettingsDialogFragment.class.getSimpleName();

    private final int MAX_WORK = 60;
    private final int MIN_WORK = 5;
    private final int MAX_REST = 25;
    private final int MIN_REST = 1;

    private final String WORK_THEME = "work_theme";
    private final String WORK_THEME_BORDER = "work_theme_border";
    private final String WORK_TEXT_COLOR = "work_text_color";

    private Settings mSettings;
    private Gson gson;
    private int mWorkTime;
    private int mRestTime;
    private int mWorkTheme;
    private Boolean mHasVibration;
    private SeekBar workSeekBar;
    private SeekBar restSeekBar;
    private TextView workText;
    private TextView restText;
    private RadioButton defaultTheme;
    private RadioButton blackTheme;
    private RadioButton blueTheme;
    private CheckBox vibratorCheckbox;
    private ImageButton applyButton;
    private OnSettingsAccessedListener mListener;


    public static SettingsDialogFragment newInstance(String settings) { //From Gson, the json representation of our Settings object
        SettingsDialogFragment frag = new SettingsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Settings", settings);
        frag.setArguments(bundle);
        return frag;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSettingsAccessedListener) activity;
        } catch (ClassCastException e) {
            Log.d(TAG, e.getMessage());
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        gson = new Gson();
        String jsonsettings = getArguments().getString("Settings");
        mSettings = gson.fromJson(jsonsettings, Settings.class);

        mWorkTime = mSettings.getWorkTime();
        mRestTime = mSettings.getRestTime();
        mWorkTheme = mSettings.getWorkTheme();
        mHasVibration = mSettings.hasVibration();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.settings_popup, null);

        workSeekBar = (SeekBar) v.findViewById(R.id.workSeekBar);
        restSeekBar = (SeekBar) v.findViewById(R.id.restSeekBar);
        workText = (TextView) v.findViewById(R.id.workText);
        restText = (TextView) v.findViewById(R.id.restText);
        defaultTheme = (RadioButton) v.findViewById(R.id.defaultThemeButton);
        blackTheme = (RadioButton) v.findViewById(R.id.blackThemeButton);
        blueTheme = (RadioButton) v.findViewById(R.id.blueThemeButton);
        applyButton = (ImageButton) v.findViewById(R.id.applyButton);
        vibratorCheckbox = (CheckBox) v.findViewById(R.id.vibrateCheckBox);

        //setup
        setSeekBars(mWorkTime, mRestTime); //seeks bar values to current user settings
        setThemeSetting(mWorkTheme); //current work theme is checked
        setVibrationSetting(mHasVibration); //current vibration choice checked/not checked


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Integer> map = getWorkThemeChoice();

                //update settings and pass it to main activity
                mSettings.updateSettings(
                        mWorkTime,
                        mRestTime,
                        map.get(WORK_THEME),
                        map.get(WORK_THEME_BORDER),
                        map.get(WORK_TEXT_COLOR),
                        mHasVibration
                );

                mListener.OnSettingsAccessed(mSettings);
                dismiss();

            }
        });

        builder.setView(v);
        return builder.create();
    }


    //get the work theme settings and the accompanying text color/border theme
    public Map<String, Integer> getWorkThemeChoice() {

        Map<String, Integer> workTheme = new HashMap<>();

        if (defaultTheme.isChecked()) {
            workTheme.put(WORK_THEME, R.drawable.theme_green_screen);
            workTheme.put(WORK_THEME_BORDER, R.drawable.theme_default_work_border);
            workTheme.put(WORK_TEXT_COLOR, R.color.defaultTimerTextColor);

        } else if (blackTheme.isChecked()) {
            workTheme.put(WORK_THEME, R.drawable.theme_brown_screen);
            workTheme.put(WORK_THEME_BORDER, R.drawable.theme_black_work_border);
            workTheme.put(WORK_TEXT_COLOR, R.color.blackTimerThemeTextColor);

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

            case R.drawable.theme_brown_screen:
                blackTheme.setChecked(true);
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
