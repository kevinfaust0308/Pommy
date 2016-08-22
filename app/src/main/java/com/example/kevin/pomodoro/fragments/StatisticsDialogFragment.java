package com.example.kevin.pomodoro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.kevin.pomodoro.Progress;
import com.example.kevin.pomodoro.R;
import com.google.gson.Gson;

import java.util.Random;

public class StatisticsDialogFragment extends DialogFragment {

    private Progress mProgress;

    private int mAmountOfTimesOpened;
    private int mCompletedWorkRounds;
    private int mCompletedRestRounds;

    private TextView statText;

    private TextView workCount;
    private TextView restCount;
    private TextView openedCount;

    public static StatisticsDialogFragment newInstance(String progress) { //From Gson, the json representation of our Progress object
        StatisticsDialogFragment frag = new StatisticsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Progress", progress);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String jsonsettings = getArguments().getString("Progress");
        mProgress = new Gson().fromJson(jsonsettings, Progress.class);
        mAmountOfTimesOpened = mProgress.getAmountOfTimesOpened();
        mCompletedWorkRounds = mProgress.getCompletedWorkRounds();
        mCompletedRestRounds = mProgress.getCompletedRestRounds();


        AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.stats_popup, null);

        workCount = (TextView) v.findViewById(R.id.completedWorkCount);
        workCount.setText(String.valueOf(mCompletedWorkRounds));

        restCount = (TextView) v.findViewById(R.id.completedRestCount);
        restCount.setText(String.valueOf(mCompletedRestRounds));

        openedCount = (TextView) v.findViewById(R.id.openedAppCount);
        openedCount.setText(String.valueOf(mAmountOfTimesOpened));

        int i = new Random().nextInt(4);
        statText = (TextView) v.findViewById(R.id.statText);
        statText.setText(getResources().getStringArray(R.array.StatisticTexts)[i]);

        dia.setView(v).setPositiveButton("OK", null);

        return dia.create();
    }
}



