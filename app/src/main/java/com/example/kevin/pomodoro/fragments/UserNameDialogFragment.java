package com.example.kevin.pomodoro.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kevin.pomodoro.R;

/**
 * Created by Kevin on 2015-12-20.
 */
public class UserNameDialogFragment extends DialogFragment {

    public static final String TAG = UserNameDialogFragment.class.getSimpleName();
    private ImageButton mImageButton;
    private TextInputLayout mTextInputLayout;
    private EditText mEditText;
    private String mName;
    private OnNameTypedListener mListener;

    /*
    When fragment and activity gets bound, get a handle on the activity and store it
    The activity now acts as a listener which will run the OnNameTypedListener interface method
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNameTypedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.username_popup, null);
        mImageButton = (ImageButton) v.findViewById(R.id.confirm);
        mEditText = (EditText) v.findViewById(R.id.editText);
        mTextInputLayout = (TextInputLayout) v.findViewById(R.id.textInputLayout);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mEditText.getText().toString();
                if (validateName(mName)) {
                    dismiss();
                    //callback method
                    mListener.onNameTyped(mName);
                } else {
                    mTextInputLayout.setError("Please enter at least one character");
                }
            }
        });

        builder.setView(v);
        return builder.create();
    }

    public boolean validateName(String name) {
        return (name.length() > 0);
    }

    public interface OnNameTypedListener {
        void onNameTyped(String name);
    }
}
