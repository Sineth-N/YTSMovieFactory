package com.android.dev.sineth.ytsmoviefactory.View;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sineth on 2/20/2016.
 */
public class YTSApplication extends Application {
    private static YTSApplication ytsApplication=null;
    @Override
    public void onCreate() {
        super.onCreate();
        ytsApplication=this;
    }
    public static YTSApplication getInstance(){
        return ytsApplication;
    }
    public static Context getAppContext(){
        return ytsApplication.getApplicationContext();
    }
}
