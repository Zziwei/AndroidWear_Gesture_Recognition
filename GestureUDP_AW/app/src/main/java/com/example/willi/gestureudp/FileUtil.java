package com.example.willi.gestureudp;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by willi on 6/17/2016.
 */
public class FileUtil {

    /**
     * TASK load the template from raw resource and initial the GestureNodel
     * @param context
     * @return
     * @throws IOException
     */
    public static GestureModel loadTemplate(MainActivity context) throws IOException {
        GestureModel retGesture = new GestureModel();
        retGesture.setName("Point");
        ArrayList<DataMap> template = new ArrayList<>();

        Resources resources = context.getResources();
        InputStream is = resources.openRawResource(R.raw.template);
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String temp = null;
        temp = br.readLine();
        String[] stringArray = temp.split(" ");
        retGesture.setThreshold(Double.parseDouble(stringArray[0]));
        retGesture.setTimespan(Integer.parseInt(stringArray[1]));
        temp = br.readLine();
        int i = 1;
        while(temp != null){
            i++;
            stringArray = temp.split(" ");
            DataMap map = new DataMap();
            DataNode node = new DataNode(stringArray);
            map.putFloatArray("value", node.toFloatArray());
            template.add(map);
            temp = br.readLine();
        }
        br.close();
        retGesture.setGestureData(template);
        retGesture.setM(template.size());
        return retGesture;
    }
}