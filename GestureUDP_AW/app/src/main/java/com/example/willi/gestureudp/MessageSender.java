/* This class sends message from wearable to Android phone. Currently, we send
the training template to android phone as well as the notification whenever
a gesture is recognized.
 */
package com.example.willi.gestureudp;

import android.content.Context;
import android.widget.Toast;

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
    private InetAddress serverAddress = null; // the address of server
    private int servPort = 4569;

    // the ip address of the server PC or android phone
    private static final String Server_IP = "10.9.9.118"; //"192.168.0.106";




    // Since asynchronous/blocking functions should not run on the UI thread.
    private ExecutorService executorService;

    private MessageSender() {
        executorService = Executors.newCachedThreadPool();
        try {
            serverAddress = InetAddress.getByName(Server_IP);
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
    public static synchronized MessageSender getInstance() {
        if (instance == null) {
            instance = new MessageSender();
        }
        return instance;
    }

    /**
     * send data to server by UDP
     * @param data the data need to be sent to server
     */
    public void send(final byte[] data) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                DatagramPacket p = new DatagramPacket(data, data.length, serverAddress,
                        servPort);
                try {
                    socket.send(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * send data to server by UDP
     * @param message the data need to be sent to server
     */
    public void send(final String message, final Context context) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                int msg_length = message.length();
                byte[] messageByte = message.getBytes();
                DatagramPacket p = new DatagramPacket(messageByte, msg_length, serverAddress,
                        servPort); // create DatagramPacket with message as data
                try {
                    socket.send(p); // try to send
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
