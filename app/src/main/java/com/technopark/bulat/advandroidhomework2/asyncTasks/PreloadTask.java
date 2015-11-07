package com.technopark.bulat.advandroidhomework2.asyncTasks;

import android.os.AsyncTask;


import java.util.concurrent.TimeUnit;

/**
 * Created by bulat on 05.11.15.
 */
public class PreloadTask extends AsyncTask<Void, Void, Void> {
    private FragmentCallback fragmentCallback;

    public PreloadTask(FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {}
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fragmentCallback.onTaskDone();
    }
}
