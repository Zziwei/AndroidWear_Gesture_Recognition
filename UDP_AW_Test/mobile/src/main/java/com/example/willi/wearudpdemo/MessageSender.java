/* This class sends message from wearable to Android phone. Currently, we send
the training template to android phone as well as the notification whenever
a gesture is recognized.
 */
package com.example.willi.wearudpdemo;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageSender {

    private DatagramSocket socket = null;
    private InetAddress serverAddress = null; // the watch address
    private int servPort = 4568;

    // Since asynchronous/blocking functions should not run on the UI thread.
    private ExecutorService executorService;

    private MessageSender(Context context) {
        executorService = Executors.newCachedThreadPool();
        try {
            serverAddress = InetAddress.getByName("192.168.0.101");// the address of the watch
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            socket = new DatagramSocket(servPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    // It's a singleton class.
    private static MessageSender instance = null;
    public static synchronized MessageSender getInstance(Context context) {
        if (instance == null) {
            instance = new MessageSender(context.getApplicationContext());
        }
        return instance;
    }

    public void send(final String message) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                int msg_length = message.length();
                byte[] messageByte = message.getBytes();
                DatagramPacket p = new DatagramPacket(messageByte, msg_length, serverAddress,
                        servPort);
                try {
                    socket.send(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
