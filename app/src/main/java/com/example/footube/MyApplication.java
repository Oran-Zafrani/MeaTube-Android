package com.example.footube;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}

