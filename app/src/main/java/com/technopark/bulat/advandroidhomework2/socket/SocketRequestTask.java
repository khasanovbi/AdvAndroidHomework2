package com.technopark.bulat.advandroidhomework2.socket;

import android.os.AsyncTask;

import com.technopark.bulat.advandroidhomework2.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

/**
 * Created by bulat on 06.11.15.
 */
public class SocketRequestTask extends AsyncTask<String, Integer, String> {
    private WeakReference<RequestListener> mListener;
    private int mErrorStringID;
    private static final String logTag = "Socket";

    public SocketRequestTask(RequestListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestString = (params != null && params.length > 0 ? params[0] : "");
        try {
            GlobalSocket globalSocket = GlobalSocket.getInstance();
            return globalSocket.performRequest(requestString);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            mErrorStringID = R.string.unknown_host;
        } catch (IOException ex) {
            ex.printStackTrace();
            mErrorStringID = R.string.error_connecting;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (!isCancelled()) {
            RequestListener l = mListener.get();
            if (l != null) {
                if (s != null) {
                    l.onRequestResult(s);
                } else {
                    l.onRequestError(mErrorStringID);
                }
            }
        }
    }
}
