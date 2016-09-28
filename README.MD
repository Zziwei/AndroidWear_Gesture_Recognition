'GestureUDP_AW' is the project that sends recognized 'Pointing geture' via UDP. 

To run the program on Android Wear, please follow these steps:
1. Install Android Studio: https://developer.android.com/studio/index.html
2. Configure Watch and Phone to enable debug over Bluetooth: https://developer.android.com/training/wearables/apps/bt-debugging.html
   Only first two sections need to be configured. Sometimes, the command in Step 4 of second section 'Set Up a Debugging Session' do 
   not work. Please the following commands instead.
      adb forward tcp:4444 localabstract:/adb-hub
      adb connect 127.0.0.1:4444
3. Run the project in Android Studio and the software will be installed on 'Android Wear'.

