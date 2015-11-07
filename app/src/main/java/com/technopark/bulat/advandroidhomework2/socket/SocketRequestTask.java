package com.technopark.bulat.advandroidhomework2.socket;

import android.os.AsyncTask;

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
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by bulat on 06.11.15.
 */
public class SocketRequestTask extends AsyncTask<String, Integer, String> {
    private WeakReference<RequestListener> mListener;
    private int mErrorStringID;

    public SocketRequestTask(RequestListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (params != null && params.length > 1) {
            String address = params[0];
            Integer port = Integer.valueOf(params[1]);
            try {
                return port == 80 ? performDefaultConnection(address, port) :
                        performSecureConnection(address, port);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                mErrorStringID = R.string.unknown_host;
            } catch (IOException ex) {
                ex.printStackTrace();
                mErrorStringID = R.string.error_connecting;
            }
        } else {
            mErrorStringID = R.string.too_few_params;
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

    protected byte[] getRequestString(String address) {
        String request = "GET / HTTP/1.1\r\nHost: " + address + "\r\nConnection: Close\r\n\r\n";
        return request.getBytes(Charset.forName("UTF-8"));
    }

    protected String performDefaultConnection(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        InputStream is = new BufferedInputStream(socket.getInputStream());
        OutputStream os = socket.getOutputStream();
        os.write(getRequestString(address));
        os.flush();
        String output = readInputStream(is);
        is.close();
        os.close();
        socket.close();
        return output;
    }

    protected String performSecureConnection(String address, int port) throws IOException {
        SocketFactory sf = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) sf.createSocket(address, port);
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession s = socket.getSession();
        if (!hv.verify(address, s)) {
            throw new SSLHandshakeException("Expected " + address + ", " + "found " + s.getPeerPrincipal());
        }
        InputStream is = new BufferedInputStream(socket.getInputStream());
        OutputStream os = socket.getOutputStream();
        os.write(getRequestString(address));
        os.flush();
        String output = readInputStream(is);
        is.close();
        os.close();
        socket.close();
        return output;
    }
}
