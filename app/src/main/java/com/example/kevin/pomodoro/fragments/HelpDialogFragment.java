package com.example.kevin.pomodoro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.kevin.pomodoro.R;

public class HelpDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_popup, null))
                .setPositiveButton("OK", null);
        return builder.create();
    }
}
