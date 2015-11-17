package com.technopark.bulat.advandroidhomework2.network.socket;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.network.request.RequestMessage;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.response.events.EnterEvent;
import com.technopark.bulat.advandroidhomework2.network.response.events.LeaveEvent;
import com.technopark.bulat.advandroidhomework2.network.response.events.MessageEvent;
import com.technopark.bulat.advandroidhomework2.network.response.messages.Auth;
import com.technopark.bulat.advandroidhomework2.network.response.messages.ChannelList;
import com.technopark.bulat.advandroidhomework2.network.response.messages.EnterChat;
import com.technopark.bulat.advandroidhomework2.network.response.messages.LeaveChat;
import com.technopark.bulat.advandroidhomework2.network.response.messages.Registration;
import com.technopark.bulat.advandroidhomework2.network.response.messages.SendMessage;
import com.technopark.bulat.advandroidhomework2.network.response.messages.UserInfo;
import com.technopark.bulat.advandroidhomework2.network.response.messages.Welcome;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observable;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bulat on 11.11.15.
 */
public class GlobalSocket implements SocketParams, Observable {
    private List<Observer> observers = new ArrayList<>();
    private static volatile GlobalSocket instance;
    private static final String LOG_TAG = "GlobalSocket";
    private static Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread asyncThread;

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

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(ResponseMessage responseMessage) {
        for (Observer observer : observers) {
            observer.handleResponseMessage(responseMessage);
        }
    }

    private GlobalSocket() {
        turnOnAsyncThread();
    }
    public void turnOnAsyncThread() {
        asyncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Log.d(LOG_TAG, "AsyncThreadRun");
                    String responseString = null;
                    try {
                        Thread.sleep(SOCKET_CHECK_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    responseString = readInputStream();
                    if (responseString != null) {
                        try {
                            /* Когда из socket'а прочитано более 1 строки. */
                            while (responseString.length() > 0) {
                                JSONObject splitResponseJson =  new JSONObject(responseString);
                                int splitResponseStringLength = splitResponseJson.toString().length();
                                ResponseMessage responseMessage = getResponseMessage(splitResponseJson);
                                if (responseMessage != null) {
                                    notifyObservers(responseMessage);
                                }
                                responseString = responseString.substring(splitResponseStringLength);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        asyncThread.start();
    }

    public void connect() {
        Log.d(LOG_TAG, "connect");
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            socket = new Socket(HOST, PORT);
            socket.setSoTimeout(SOCKET_READ_KEEPALIVE);
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readInputStream() {
        if (socket == null || !socket.isConnected()) {
            connect();
        }
        String output = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int read;
            byte[] data = new byte[16384];
            while (true) {
                try {
                    read = inputStream.read(data, 0, data.length);
                } catch (SocketTimeoutException e) {
                    break;
                }
                if (read > 0) {
                    outputStream.write(data, 0, read);
                } else {
                    if (read == 0) {
                        break;
                    } else {
                        connect();
                    }
                }
                try {
                    JSONObject jsonObject = new JSONObject(outputStream.toString("utf-8"));
                    break;
                } catch (JSONException ignored) {
                }

            }
            output = outputStream.toString("utf-8");
            if (output.equals("")) {
                output = null;
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private class Request extends Thread {
        private RequestMessage requestMessage;

        Request(RequestMessage requestMessage) {
            this.requestMessage = requestMessage;
        }

        @Override
        public void run() {
            String requestString = requestMessage.getRequestString();
            Log.d(LOG_TAG, "Request: " + requestString);
            if (socket == null || !socket.isConnected()) {
                connect();
            }
            try {
                outputStream.write(requestString.getBytes(Charset.forName("UTF-8")));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void performAsyncRequest(RequestMessage requestMessage) {
        (new Request(requestMessage)).start();
    }

    public ResponseMessage getResponseMessage(JSONObject splitResponseJson) {
        try {
            String action = splitResponseJson.getString("action");
            JSONObject jsonData;
            if (!action.equals("welcome")) {
                jsonData = splitResponseJson.getJSONObject("data");
                return new ResponseMessage(action, jsonData);
            } else {
                Welcome welcome = new Welcome();
                welcome.parse(splitResponseJson);
                return null;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}