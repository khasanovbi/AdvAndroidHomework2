package com.technopark.bulat.advandroidhomework2.asyncTasks;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by bulat on 05.11.15.
 */
public class PreloadTask extends AsyncTask<Void, Void, Void> {
    private OnPreloadTaskDone fragmentCallback;
    private static final int SLEEP_TIME = 2;

    public PreloadTask(OnPreloadTaskDone fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            TimeUnit.SECONDS.sleep(SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fragmentCallback.onPreloadTaskDone();
    }
}
