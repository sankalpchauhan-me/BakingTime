package me.sankalpchauhan.bakingtime.utilstesting;

import android.os.Handler;

import androidx.annotation.Nullable;

public class MessageDelayer {
    private static final int DELAY = 5000;

    public static void messageProcessor(final String message, final DelayCallback callback, @Nullable SimpleIdlingResource idlingResource) {
        if (idlingResource != null) {
            idlingResource.idleStae(false);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.complete(message);
                    if (idlingResource != null) {
                        idlingResource.idleStae(true);
                    }
                }
            }
        }, DELAY);
    }

    public interface DelayCallback {
        void complete(String text);
    }
}
