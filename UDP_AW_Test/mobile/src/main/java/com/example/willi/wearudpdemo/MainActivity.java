package com.example.willi.wearudpdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private static final int ECHOMAX = 512;
    private DatagramSocket socket = null;
    private InetAddress clientAddress = null; // client address
    private int servPort = 4567;
    private Button messageButton;

    private MessageSender dataSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        messageButton = (Button) findViewById(R.id.mesg);
        dataSender = MessageSender.getInstance(this);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSender.send("message from phone!!");
            }
        });
        new RecieveThread().execute();
    }

    private DatagramPacket packet;

    //the thread to listen for data
    private class RecieveThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                socket = new DatagramSocket(servPort);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

            while (true) {
                try {
                    socket.receive(packet); // Receive packet from client
                    publishProgress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientAddress = packet.getAddress();
                packet.setLength(ECHOMAX);
            }
        }

        protected void onProgressUpdate(Void... progress) {
            text.setText(packet.getAddress().getHostAddress() + "\n" + new String(packet.getData()));
        }
    }
}
