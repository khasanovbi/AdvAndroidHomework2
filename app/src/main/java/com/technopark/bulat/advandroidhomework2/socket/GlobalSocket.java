package com.technopark.bulat.advandroidhomework2.socket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by bulat on 11.11.15.
 */
public class GlobalSocket {
    private static volatile GlobalSocket instance;
    private static final String host = "188.166.49.215";
    private static final int port = 7777;
    private static String logTag = "GlobalSocket";

    public Socket socket;

    public static GlobalSocket getInstance() {
        GlobalSocket localInstance = instance;
        if (localInstance == null) {
            synchronized (GlobalSocket.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GlobalSocket();
                }
            }
        }
        return localInstance;
    }

    private GlobalSocket() {
        connect();
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            InputStream is = new BufferedInputStream(socket.getInputStream());
            String output = readInputStream(is);
            Log.d(logTag, output);
            //is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[16384];
        while (true) {
            read = is.read(data, 0, data.length);
            if (read > 0) {
                outputStream.write(data, 0, read);
            } else {
                break;
            }
            try {
                JSONObject jsonObject = new JSONObject(outputStream.toString("utf-8"));
                break;
            } catch (JSONException ignored) {}
        }

        outputStream.flush();
        return outputStream.toString("utf-8");
    }

    protected byte[] getRequestBytes(String requestString) {
        return requestString.getBytes(Charset.forName("UTF-8"));
    }

    protected String performRequest(String requestString) throws IOException {
        if (socket == null || !socket.isConnected()) {
            GlobalSocket.getInstance().connect();
        }
        InputStream is = new BufferedInputStream(socket.getInputStream());
        OutputStream os = socket.getOutputStream();
        os.write(getRequestBytes(requestString));
        os.flush();
        String output = readInputStream(is);
        Log.d(logTag, "Response: " + output);
        return output;
    }
}
