# Environment Setup

To run the program on Android Wear, please follow these steps:<br \>
1. Install Android Studio: https://developer.android.com/studio/index.html <br \>
2. Configure Watch and Phone to enable debug over Bluetooth: https://developer.android.com/training/wearables/apps/bt-debugging.html. Only first two sections need to be configured. Sometimes, the commands in Step 4 of second section 'Set Up a Debugging Session' do not work. Please use the following commands instead. <br \>
   ```
   adb forward tcp:4444 localabstract:/adb-hub
   adb connect 127.0.0.1:4444
   ```
   
3.Run the project in Android Studio and the software will be installed on 'Android Wear'. <br \>

# Projects description

There are three projects in this Repo. <br \>
1. GestureUDP_AW: This project reads data from Watch IMU and recognizes 'Pointing' gesture. Once the gesture is recognized, a message
will be sent to watch screen via UDP. <br \>
2. PCserver: This project is Python program running on PC to receive data from Watch via UDP. <br \>
3. UDP_AW_Test: This project sends string 'UDP test' every one second via UDP. We have tested it with Watch and assume it will work with 
emulator. <br \>

# Workflow description

"GestureUDP_AW" reads IMU data from watch and recognized one hard coded simple gesture 'Pointing Gesture'. Once the gesture is recognized, a message is sent to PC server via UDP. It can be extended to more gestures. <br \>
This video shows how the gesture is performed. <br \>
[![Gesture Recognition on Moto360](http://img.youtube.com/vi/EyYFwvkQLa4/0.jpg)](https://youtu.be/EyYFwvkQLa4)
