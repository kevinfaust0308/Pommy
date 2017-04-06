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
import com.example.kevin.pomodoro.R2;
import com.google.gson.Gson;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsDialogFragment extends DialogFragment {

    @BindView(R2.id.statText)
    TextView statText;
    @BindView(R2.id.completedWorkCount)
    TextView workCount;
    @BindView(R2.id.completedRestCount)
    TextView restCount;

    private Progress mProgress;

    private int mCompletedWorkRounds;
    private int mCompletedRestRounds;

    public static StatisticsDialogFragment newInstance(String progress) { //From Gson, the json representation of our Progress object
        StatisticsDialogFragment frag = new StatisticsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Progress", progress);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.stats_popup, null);
        ButterKnife.bind(this, v);

        String jsonsettings = getArguments().getString("Progress");
        mProgress = new Gson().fromJson(jsonsettings, Progress.class);
        mCompletedWorkRounds = mProgress.getCompletedWorkRounds();
        mCompletedRestRounds = mProgress.getCompletedRestRounds();

        workCount.setText(String.valueOf(mCompletedWorkRounds));
        restCount.setText(String.valueOf(mCompletedRestRounds));

        int i = new Random().nextInt(4);
        statText.setText(getResources().getStringArray(R.array.StatisticTexts)[i]);

        AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
        dia.setView(v).setPositiveButton("OK", null);
        return dia.create();
    }
}



