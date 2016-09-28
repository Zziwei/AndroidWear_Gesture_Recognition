package com.example.willi.gestureudp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements SensorEventListener {

    private WatchViewStub mStub;
    private Button switchButton;
    private SensorManager mSensorManager;
    //private Sensor mSensor;
    private Sensor accSensor;
    private Sensor gyroSensor;

    private boolean accStatus = false;
    private boolean gyrStatus = false;
    private DataNode dataNode;

    private long pktNum;

    private int app_status = Config.APP_STATUS_NONE;

    private ArrayList<SPRING> springList;
    private MessageSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize the UI
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        mStub = stub;
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                switchButton = (Button) stub.findViewById(R.id.switch_button);
                switchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (app_status) {
                            case Config.APP_STATUS_NONE: {
                                startRecognition();
                                break;
                            }
                            case Config.APP_STATUS_RECOGNITION: {
                                stopRecognition();
                                break;
                            }
                        }
                    }
                });
            }
        });

        //get the socket sender
        sender = MessageSender.getInstance();

        //initialize sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    /**
     * before recognition, initialize the signal processing parameters, such as the template gesture model
     * and start sensors
     */
    private void startRecognition() {
        app_status = Config.APP_STATUS_RECOGNITION;

        pktNum = 0;

        GestureModel gestureModel = null ;
        try {
            gestureModel = FileUtil.loadTemplate(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        springList = new ArrayList<>();
        springList.add(new SPRING(gestureModel));

        switchButton.setText(Config.SWITCH_BUTT_TXT_STOP_RECOGNITION);
        switchButton.setBackgroundColor(Color.RED);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * stop recognizing, unregister sensors
     */
    private void stopRecognition() {
        app_status = Config.APP_STATUS_NONE;

        switchButton.setBackgroundColor(Color.GREEN);
        switchButton.setText(Config.SWITCH_BUTT_TXT_START_RECOGNITION);
        mStub.setBackgroundColor(Color.WHITE);
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        if(!accStatus && !gyrStatus) {
            dataNode = new DataNode();
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accStatus = true;
            dataNode.setACC(event);
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyrStatus = true;
            dataNode.setGYR(event);
        }

        //do not do signal processing until we get both acc data and gyro data
        if (accStatus && gyrStatus) {
            accStatus = false;
            gyrStatus = false;

            long currentTime = System.currentTimeMillis();
            dataNode.setTimeStamp(currentTime);
            dataNode.setPktNum(++pktNum);

            switch (app_status) {
                case Config.APP_STATUS_RECOGNITION: {
                    for (int i = 0; i < springList.size(); i++) {   //this is for the situation that several gestures are recognized at the same time
                        int result = springList.get(i).SignalProcess(dataNode, (int) pktNum);
                        if (result == Config.SPRING_TYPE_GESTURE) {
                            Toast.makeText(this, springList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            sender.send(springList.get(i).getName() + "found!", this);  //found a gesture, send message to server through UDPs
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
