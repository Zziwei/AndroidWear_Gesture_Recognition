/* This is the SPRING DTW algorithm we are using for training and gesture
recognition. The details of this algorithm can be found here:
http://www.cs.cmu.edu/~christos/PUBLICATIONS/ICDE07-spring.pdf
 */

package com.example.willi.gestureudp;

import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by willi on 6/28/2016.
 */
public class SPRING {
    private GestureModel template;                  //template data
    private ArrayList<Double> distanceArray;
    private ArrayList<Double> distanceArrayLast;
    private ArrayList<Integer> startArray;
    private ArrayList<Integer> startArrayLast;
    private ArrayList<Long> timeArray;
    private ArrayList<Long> timeArrayLast;
    private double dmin;
    private int ts;
    private int te;
    private long times;
    private long timee;
    private double threshold;                       //threshold for the algorithm
    private int id;
    private long timeLimit;                         //gesture time span limitation
    private String name;                            //gesture name
    private int m;                                  //template length
    private double findGestureDist;

    public double getFindGestureDist() {
        return findGestureDist;
    }

    public void setFindGestureDist(double findGestureDist) {
        this.findGestureDist = findGestureDist;
    }

    public int getTimeSpan() {
        return (int) (this.timee - this.times);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SPRING(GestureModel template) {
        this.template = template;
        m = template.getM();
        threshold = template.getThreshold();
        distanceArray = new ArrayList<>();
        startArray = new ArrayList<>();
        timeArray = new ArrayList<>();
        distanceArrayLast = new ArrayList<>();
        startArrayLast = new ArrayList<>();
        timeArrayLast = new ArrayList<>();
        dmin = Double.MAX_VALUE;
        for(int i = 0; i <= template.getM(); i++)
        {
            distanceArrayLast.add(Double.MAX_VALUE);
            startArrayLast.add(0);
            timeArrayLast.add(0L);
        }
        ts = 1;
        te = 1;
        times = 0;
        timee = 0;
        timeLimit = template.getTimespan();
        name = template.getName();
    }

    /**
     *TASK:the process struct of one kind of gesture
     *grp:the process struct of the specific type of gesture
     *xt:the current data inputed
     *position:the position of thr current data in the queue
     *usePath:whether need to compute the warping path
     */
    private void updateArray(int position, DataNode node) {
        distanceArray.add(0d);
        startArray.add(position);
        timeArray.add(node.getTimeStamp());
        ArrayList<DataMap> templateData = template.getGestureData();
        for(int i = 1; i < m + 1; i++)
        {
            int startTmp;
            long timeTmp;
            double distanceTmp = Math.pow((node.getAccx() - templateData.get(i - 1).getFloatArray("value")[0]), 2)
            + Math.pow((node.getAccy() - templateData.get(i - 1).getFloatArray("value")[1]), 2)
                    + Math.pow((node.getAccz() - templateData.get(i - 1).getFloatArray("value")[2]), 2)
            + Math.pow((node.getGyrx() - templateData.get(i - 1).getFloatArray("value")[3]), 2)
                    + Math.pow((node.getGyry() - templateData.get(i - 1).getFloatArray("value")[4]), 2)
            + Math.pow((node.getGyrz() - templateData.get(i - 1).getFloatArray("value")[5]), 2);

            if(distanceArray.get(i - 1) <= distanceArrayLast.get(i))
            {
                if(distanceArray.get(i - 1) <= distanceArrayLast.get(i - 1))
                {
                    distanceTmp += distanceArray.get(i - 1);
                    startTmp = startArray.get(i - 1);
                    timeTmp = timeArray.get(i - 1);
                }
                else
                {
                    distanceTmp += distanceArrayLast.get(i - 1);
                    startTmp = startArrayLast.get(i - 1);
                    timeTmp = timeArrayLast.get(i - 1);
                }
            }
            else
            {
                if(distanceArrayLast.get(i) <= distanceArrayLast.get(i - 1))
                {
                    distanceTmp += distanceArrayLast.get(i);
                    startTmp = startArrayLast.get(i);
                    timeTmp = timeArrayLast.get(i);
                }
                else
                {
                    distanceTmp += distanceArrayLast.get(i - 1);
                    startTmp = startArrayLast.get(i - 1);
                    timeTmp = timeArrayLast.get(i - 1);
                }
            }
            distanceArray.add(distanceTmp);
            startArray.add(startTmp);
            timeArray.add(timeTmp);
        }
    }

    /**
     *TASK:the main part of the SPRING DTW algorithm
     *grProcess:the process struct of the specific type of gesture
     *xt:the current data
     *position:the position of the current data in the queue
     *isSkip:(ignore this variable)
     *isWriteDistance:whether output the DTW distance to a txt file
     *isPrint:whether print the DTW details in the screen
     *usePath:whether compute the warping path
     *pathList:the point of the variable to contain the warping path
     */
    public int SignalProcess(DataNode xt, int position) {
        int is_gesture = Config.SPRING_TYPE_NONE;

        updateArray(position, xt);

        //check whether the temporary optimal subsequence is a right one
        if(dmin <= threshold)
        {
            boolean is_di_largerthan_dmin = true;
            boolean is_si_largerthan_te = true;

            int i = 0;
            for(i = m; i <= m; i++)
            {
                if(distanceArray.get(i) < dmin)
                {
                    is_di_largerthan_dmin = false;
                }

                if(is_di_largerthan_dmin == false /*&& is_si_largerthan_te == false*/)
                {
                    break;
                }
            }

            if(is_di_largerthan_dmin == true /*|| is_si_largerthan_te == true*/)
            {
                //check the time span of the temporary optimal subsequence
                long timeGap = timee - times;
                if(timeGap >= timeLimit)
                {
                    //report the right optimal subsequence
                    Log.d("gesture", "!!!!!!!!!!!@@@@@@@@@@@##############\nname: " + name + "\ndmin: "
                            + dmin + "\nts: " + ts + "\nte: " + te + "\nposition: " + position
                            + "\ntime gap: " + (timee - times) + "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    setFindGestureDist(dmin);

                    //reinitialize the dmin,d
                    dmin = Double.MAX_VALUE;
                    distanceArray = new ArrayList<>();
                    distanceArray.add(0d);
                    for(i = 1; i <= m; i++)
                    {
                        distanceArray.add(Double.MAX_VALUE);
                    }
                    is_gesture = Config.SPRING_TYPE_GESTURE;
                }
            }
        }


        //check whether the current subsequence can be determined as a temporary optimal subsequence
        if(distanceArray.get(m) <= threshold && distanceArray.get(m) < dmin)
        {
            dmin = distanceArray.get(m);
            ts = startArray.get(m);
            te = position;
            times = timeArray.get(m);
            timee = xt.getTimeStamp();
        }

        Log.d("spring", xt.getPktNum() + "::distance = " + distanceArray.get(m) + "::start = "
                + startArray.get(m) + "::start = " + ts + "::end = " + te + "\n");

        // replace the d with d', and s with s'
        distanceArrayLast = distanceArray;
        distanceArray = new ArrayList<>();

        startArrayLast = startArray;
        startArray = new ArrayList<>();

        timeArrayLast = timeArray;
        timeArray = new ArrayList<>();

        return is_gesture;
    }

    /**
     * TASK generate a random node based on given node
     * @param map
     * @return
     */
    private static DataMap generateRandomNode(DataMap map) {
        DataMap newMap = new DataMap();
        float[] array = new float[6];
        for (int i = 0; i < 6; i++) {
            array[i] = (float) (map.getFloatArray("value")[i] + (Math.random() * 0.3) - 0.15);
        }
        newMap.putFloatArray("value",array);
        return newMap;
    }

    /**
     * TASK compute two sequences' DTW distance
     * @param template
     * @return
     */
    public static double computeDefaultTrainTreshold(ArrayList<DataMap> template) {
        ArrayList<DataMap> generated = new ArrayList<>();
        for (int i = 0; i < template.size(); i++) {
            switch ((int) (Math.random() * 4)) {
                case 0: {
                    break;
                }
                case 1: {
                    generated.add(generateRandomNode(template.get(i)));
                    generated.add(generateRandomNode(template.get(i)));
                    break;
                }
                default: {
                    generated.add(generateRandomNode(template.get(i)));
                    break;
                }
            }
        }

        return computeTraditionalDTW(template, generated) * 1.4;
    }

    /**
     *TASK:the traditional DTW distance computation
     *og:the structure of a defined gesture
     *head:a sequence of user data
     */
    public static double computeTraditionalDTW(ArrayList<DataMap> gestureA, ArrayList<DataMap> gestureB)
    {
        int templateLength = gestureA.size();
        int inputLength = gestureB.size();

        double[][] distanceMetrix = new double[inputLength][templateLength];

        int i,j;

        for(i = 0; i < inputLength; i++)
        {
            float[] nodeB = gestureB.get(i).getFloatArray("value");
            for(j = 0; j < templateLength; j++)
            {
                float[] nodeA = gestureA.get(j).getFloatArray("value");
                double tmpDistance = Math.pow((nodeA[0] - nodeB[0]), 2)
                        + Math.pow((nodeA[1] - nodeB[1]), 2)
                        + Math.pow((nodeA[2] - nodeB[2]), 2)
                        + Math.pow((nodeA[3] - nodeB[3]), 2)
                        + Math.pow((nodeA[4] - nodeB[4]), 2)
                        + Math.pow((nodeA[5] - nodeB[5]), 2);

                if(i == 0 || j == 0)
                {
                    distanceMetrix[i][j] = tmpDistance;
                }
                else
                {
                    if(distanceMetrix[i - 1][j] <= distanceMetrix[i][j - 1])
                    {
                        if(distanceMetrix[i - 1][j] <= distanceMetrix[i - 1][j - 1] )
                        {
                            distanceMetrix[i][j] = tmpDistance + distanceMetrix[i - 1][j];
                        }
                        else
                        {
                            distanceMetrix[i][j] = tmpDistance + distanceMetrix[i - 1][j - 1];
                        }
                    }
                    else
                    {
                        if(distanceMetrix[i][j - 1] <= distanceMetrix[i - 1][j - 1] )
                        {
                            distanceMetrix[i][j] = tmpDistance + distanceMetrix[i][j - 1];
                        }
                        else
                        {
                            distanceMetrix[i][j] = tmpDistance + distanceMetrix[i - 1][j - 1];
                        }
                    }
                }

            }
        }
        return distanceMetrix[inputLength - 1][templateLength - 1];
    }
}
