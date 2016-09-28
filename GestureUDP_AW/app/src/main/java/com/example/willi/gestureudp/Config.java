/* The configuration file for our app. It stores all the macro variables
for our gesture app.
 */
package com.example.willi.gestureudp;

/**
 * Created by willi on 6/24/2016.
 */
public class Config {
    public static final int PAGE_TYPE_SHOW_DATA = 0;
    public static final int PAGE_TYPE_SAVE_TEMPLATE = 1;
    public static final int PAGE_TYPE_GESTURE_RECOGNITION = 2;

    public static final String SAVE_TEMPLATE_PATH = "/save-template";
    public static final String GESTURE_RECOGNITION_PATH = "/gesture";

    public static final int NORMALIZATION_VALUE_ACC = 15;
    public static final int NORMALIZATION_VALUE_GYR = 10;

    public static final int APP_STATUS_NONE = 0;
    public static final int APP_STATUS_INPUT_NAME = 1;
    public static final int APP_STATUS_SAVE_TEMPLATE = 2;
    public static final int APP_STATUS_FINISH_SAVE_TEMPLATE = 3;
    public static final int APP_STATUS_TRAIN = 4;
    public static final int APP_STATUS_FINISH_TRAIN = 5;
    public static final int APP_STATUS_RECOGNITION = 6;

    public static final String SWITCH_BUTT_TXT_START_TEMPLATE_NAME = "input gesture name";
    public static final String SWITCH_BUTT_TXT_START_SAVE = "start saving template";
    public static final String SWITCH_BUTT_TXT_STOP_SAVE = "stop saving template";
    public static final String SWITCH_BUTT_TXT_START_TRAIN = "start training";
    public static final String SWITCH_BUTT_TXT_STOP_TRAIN = "stop train";
    public static final String SWITCH_BUTT_TXT_START_RECOGNITION = "start recognizing";
    public static final String SWITCH_BUTT_TXT_STOP_RECOGNITION = "stop recognizing";

    public static final int SPRING_TYPE_NONE = 0;
    public static final int SPRING_TYPE_GESTURE = 1;



}
