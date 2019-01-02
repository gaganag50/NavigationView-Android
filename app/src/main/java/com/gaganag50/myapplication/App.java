package com.gaganag50.myapplication;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingsActivity.initSettings(this);

    }
}
