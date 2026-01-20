package ro.pub.cs.systems.eim.practicaltest02v4.network;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Utilities;

public class ClientThread extends Thread {

    private final Context context;
    private final String address;
    private final int port;
    private final String word;
    private final TextView resultTextView;

    public ClientThread(Context context,
                        String address,
                        int port,
                        String word,
                        TextView resultTextView) {

        this.context = context;
        this.address = address;
        this.port = port;
        this.word = word;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            socket = new Socket(address, port);

            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            // trimitem cuvantul
            writer.println(word);
            writer.flush();

            // citim definitia
            final String response = reader.readLine();
            final String result =
                    (response != null) ? response : "No definition found";

            resultTextView.post(() -> {
                resultTextView.setText(result);

                Toast.makeText(
                        context,
                        result,
                        Toast.LENGTH_LONG
                ).show();
            });

        } catch (IOException e) {
            Log.e(Constants.TAG, "[CLIENT] " + e.getMessage());

            resultTextView.post(() ->
                    Toast.makeText(
                            context,
                            "Connection error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show()
            );

        } finally {
            if (socket != null) {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
