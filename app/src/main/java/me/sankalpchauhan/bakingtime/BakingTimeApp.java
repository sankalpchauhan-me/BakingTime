package me.sankalpchauhan.bakingtime;

import android.app.Application;
import android.content.Context;

import me.sankalpchauhan.bakingtime.config.DefaultPrefSettings;
import timber.log.Timber;

public class BakingTimeApp extends Application {
    private static BakingTimeApp instance;

    public static BakingTimeApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        //Initialization
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        DefaultPrefSettings.init(this);
    }
}
