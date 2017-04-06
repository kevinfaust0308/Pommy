package com.example.kevin.pomodoro;

/**
 * Created by Kevin on 2016-05-25.
 */
public class Progress {

    private int mCompletedWorkRounds;
    private int mCompletedRestRounds;

    public Progress() {
        mCompletedRestRounds = 0;
        mCompletedWorkRounds = 0;
    }


    public int getCompletedRestRounds() {
        return mCompletedRestRounds;
    }

    public int getCompletedWorkRounds() {
        return mCompletedWorkRounds;
    }

    public void incrementCompletedRestRounds() {
        mCompletedRestRounds++;
    }

    public void incrementCompletedWorkRounds() {
        mCompletedWorkRounds++;
    }
}
