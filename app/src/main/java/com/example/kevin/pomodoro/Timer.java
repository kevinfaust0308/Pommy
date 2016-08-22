package com.example.kevin.pomodoro;

import android.os.CountDownTimer;
import android.util.Log;

import com.example.kevin.pomodoro.enums.Mode;
import com.example.kevin.pomodoro.enums.State;

public class Timer {

    public interface TimerListener {
        void OnTimerFinish();

        void OnTimerTick(int minutes, int seconds);
    }

    private static final String TAG = CountDownTimer.class.getSimpleName();
    //timer
    private MyCountDownTimer mMyCountDownTimer;
    //static properties of timer
    private int mStartWorkTime;
    private int mStartRestTime;
    //dynamic properties of timer
    private long mMillisecondsLeft; //time left before we switch modes
    //states/modes of timer
    private State mState;
    private Mode mMode;
    //settings of timer
    private int DEFAULT_TICK_TIME = 1000;
    //listener
    private TimerListener mListener;


    //new timer only created upon app opening
    //when we first create a timer (user opens app) we will be on work mode
    public Timer(int workTime, int restTime, TimerListener listener) {
        mStartWorkTime = workTime;
        mStartRestTime = restTime;
        mMillisecondsLeft = intTimeToLongTime(workTime);
        mListener = listener;
        mMode = Mode.WORK;
    }


    //currently only used to determine if user can +1 work/rest round by checking the start time
    //when timer reaches 0, check if user worked for more than 25 min or rested for less than 10 min
    public int getMinutesStart() {
        if (getMode() == Mode.REST) {
            return mStartRestTime;
        } else {
            return mStartWorkTime;
        }
    }


    //when user returns from settings screen, reset timer to their new preferences
    public void updateTimerWithNewSettings(int workTime, int restTime) {
        mStartWorkTime = workTime;
        mStartRestTime = restTime;
        mMillisecondsLeft = intTimeToLongTime(workTime);
        mMode = Mode.WORK;
    }


    public State getState() {
        return mState;
    }


    private void setState(State state) {
        mState = state;
    }


    public Mode getMode() {
        return mMode;
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }


    //change mode and restart
    public void changeModeAndStart(Mode currentMode) {
        mMode = (currentMode == Mode.WORK) ? Mode.REST : Mode.WORK;
        restart();
    }


    public void pause() {
        if (mMyCountDownTimer != null)
            mMyCountDownTimer.cancel();
        setState(State.PAUSED);
    }


    public void play() {
        if (mMyCountDownTimer != null)
            mMyCountDownTimer.cancel();
        mMyCountDownTimer = new MyCountDownTimer(mMillisecondsLeft, DEFAULT_TICK_TIME);
        setState(State.RUNNING);
        mMyCountDownTimer.start();
    }


    public void restart() {
        if (mMyCountDownTimer != null)
            mMyCountDownTimer.cancel();
        //remaining time is the starting time
        if (getMode() == Mode.REST)
            mMyCountDownTimer = new MyCountDownTimer(intTimeToLongTime(mStartRestTime), DEFAULT_TICK_TIME);
        else
            mMyCountDownTimer = new MyCountDownTimer(intTimeToLongTime(mStartWorkTime), DEFAULT_TICK_TIME);
        setState(State.RUNNING);
        mMyCountDownTimer.start();
    }


    private long intTimeToLongTime(int num) {
        return num * 60 * 1000;
    }


    private class MyCountDownTimer extends CountDownTimer {


        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            int minutes = (int) (millisUntilFinished / 1000) / 60;
            int seconds = (int) ((millisUntilFinished / 1000) % 60);
            mMillisecondsLeft = millisUntilFinished; //need a hold of it for pause/resume

            Log.d(TAG, "Time: " + minutes + ":" + seconds);

            mListener.OnTimerTick(minutes, seconds);
        }


        @Override
        public void onFinish() {
            mListener.OnTimerFinish();
        }
    }

}
