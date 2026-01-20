package ro.pub.cs.systems.eim.practicaltest02v4.network;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Utilities;


import ro.pub.cs.systems.eim.practicaltest02v4.model.URLInformation;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {

        if (socket == null) {
            Log.e(Constants.TAG, "[COMM] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // (3c) citim cererea clientului: word
            Log.i(Constants.TAG, "[COMM] Waiting for word...");
            String word = bufferedReader.readLine();

            if (word == null || word.trim().isEmpty()) {
                printWriter.println("Error: word missing");
                printWriter.flush();
                return;
            }
            word = word.trim();

            // (3a) luam cache-ul
            HashMap<String, URLInformation> data = serverThread.getData();
            URLInformation dictionaryInformation;

            // cache hit
            if (data.containsKey(word)) {
                Log.i(Constants.TAG, "[COMM] Getting from cache...");
                dictionaryInformation = data.get(word);

            } else {
                // (3b) HTTP request
                Log.i(Constants.TAG, "[COMM] Getting from webservice...");

                //String url = Constants.DICT_SERVICE + URLEncoder.encode(word, "UTF-8");
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(word);

                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                String pageSourceCode = "";
                if (entity != null) {
                    pageSourceCode = EntityUtils.toString(entity, "UTF-8");
                }

                // cerinta: log raspuns complet
                Log.d(Constants.TAG, "[COMM] FULL RESPONSE:");
                Log.d(Constants.TAG, pageSourceCode);

                // parse: prima definitie
                //String definition = parseFirstDefinition(pageSourceCode);

                // cerinta: log definitia #1
                //Log.d(Constants.TAG, "[COMM] DEFINITION #1 = " + definition);

                // model
                dictionaryInformation = new URLInformation(word, pageSourceCode);

                // salvam in cache
                serverThread.setData(word, dictionaryInformation);
            }

            if (dictionaryInformation == null) {
                printWriter.println("Error: no definition");
                printWriter.flush();
                return;
            }

            // (3d) trimitem raspuns la client (doar definitia ceruta)
            printWriter.println(dictionaryInformation.getDefinition());
            printWriter.flush();

        } catch (Exception e) {
            Log.e(Constants.TAG, "[COMM] ERROR: " + e.getMessage(), e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, "[COMM] close: " + e.getMessage());
            }
        }
    }

}

