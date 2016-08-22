package com.example.kevin.pomodoro;

/**
 * Created by Kevin on 2016-05-25.
 */
public class Progress {

    private int mAmountOfTimesOpened;
    private int mCompletedWorkRounds;
    private int mCompletedRestRounds;

    public Progress() {
        mAmountOfTimesOpened = 0;
        mCompletedRestRounds = 0;
        mCompletedWorkRounds = 0;
    }

    public int getAmountOfTimesOpened() {
        return mAmountOfTimesOpened;
    }

    public int getCompletedRestRounds() {
        return mCompletedRestRounds;
    }

    public int getCompletedWorkRounds() {
        return mCompletedWorkRounds;
    }

    public void incrementAmountOfTimesOpened() {
        mAmountOfTimesOpened++;
    }

    public void incrementCompletedRestRounds() {
        mCompletedRestRounds++;
    }

    public void incrementCompletedWorkRounds() {
        mCompletedWorkRounds++;
    }
}
