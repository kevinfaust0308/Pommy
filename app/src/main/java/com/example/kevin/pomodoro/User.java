package com.example.kevin.pomodoro;

/**
 * Only one of these objects will exist per user
 * Upon opening app for first time, user will have a User object created and saved for them
 * This object is what will check if a user has visited this app before or not
 * <p/>
 * Created by Kevin on 2016-05-25.
 */
public class User {

    private String mName;

    public User(String username) {
        mName = username;
    }

    public String getName() {
        return mName;
    }
}
