package com.example.servicesmaps

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyGPSService: Service() {

    // Start handler
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // Do something here, e.g. start GPS, or music playing, etc.


        return START_STICKY // --> Important if you want the service to be restarted
    }

    /*
    bind handler - not needed in many cases but defined as an abstract method
    in Service, therefore must be overridden
    */
    override fun onBind(intent: Intent?): IBinder? {
        return null // This is temporary, but needs to be changed so that the service can be bound to the Activity
    }
}