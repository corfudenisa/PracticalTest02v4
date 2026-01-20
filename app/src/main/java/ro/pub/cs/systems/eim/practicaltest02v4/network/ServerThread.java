package ro.pub.cs.systems.eim.practicaltest02v4.network;



import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;

import ro.pub.cs.systems.eim.practicaltest02v4.model.URLInformation;

public class ServerThread extends Thread {

    private ServerSocket serverSocket = null;

    // (3a) cache local:
    // key = word
    // value = DictionaryInformation
    private final HashMap<String, URLInformation> data;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.e(Constants.TAG, "[SERVER] " + e.getMessage());
        }
        data = new HashMap<>();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized HashMap<String, URLInformation> getData() {
        return data;
    }

    public synchronized void setData(String word, URLInformation dictionaryInformation) {
        data.put(word, dictionaryInformation);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER] Waiting for client...");
                Socket socket = serverSocket.accept();

                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException e) {
            Log.e(Constants.TAG, "[SERVER] " + e.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, "[SERVER] close: " + e.getMessage());
            }
        }
    }
}
