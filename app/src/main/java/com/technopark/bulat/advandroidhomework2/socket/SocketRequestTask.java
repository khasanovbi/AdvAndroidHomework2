package com.technopark.bulat.advandroidhomework2.socket;

import android.os.AsyncTask;
import android.util.Log;

import com.technopark.bulat.advandroidhomework2.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Created by bulat on 06.11.15.
 */
public class SocketRequestTask extends AsyncTask<String, Integer, String> implements ConnectionParameters {
    private WeakReference<RequestListener> mListener;
    private int mErrorStringID;
    private String requestString;

    public SocketRequestTask(RequestListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(String... params) {
        if (params != null && params.length > 0) {
            requestString = params[0];
        } else {
            requestString = "";
        }
        try {
            return performConnection(host, port);
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

    private static String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[16384];

        while ((read = is.read(data, 0, data.length)) != -1) {
            outputStream.write(data, 0, read);
        }

        outputStream.flush();
        return outputStream.toString("utf-8");
    }

    protected byte[] getRequestBytes() {
        Log.d(logTag, requestString);
        return requestString.getBytes(Charset.forName("UTF-8"));
    }

    protected String performConnection(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        InputStream is = new BufferedInputStream(socket.getInputStream());
        OutputStream os = socket.getOutputStream();
        os.write(getRequestBytes());
        os.flush();
        String output = readInputStream(is);
        is.close();
        os.close();
        socket.close();
        return output;
    }
}
