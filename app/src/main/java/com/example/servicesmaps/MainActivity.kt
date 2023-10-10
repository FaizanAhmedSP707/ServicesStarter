package com.example.servicesmaps

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    var permissionsGranted = false
    var service: MyGPSService? = null

    // Add your service as an attribute of the main activity (nullable)
    val serviceConn = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MyGPSService.GPSServiceBinder).GPS_Service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Configuration.getInstance()
            .load(this, PreferenceManager.getDefaultSharedPreferences(this))

        val map1 : MapView = findViewById(R.id.map1)
        map1.controller?.setZoom(14.0)
        map1.controller?.setCenter(GeoPoint(51.05, -0.72))
        requestPermissions()

    }

    fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, LOCATION_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            permissionsGranted = true
            initService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = true
            initService()
        } else {
            AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("GPS permission denied").show()
        }
    }

    fun initService() {
        // Start and bind the service here...
        val bindIntent = Intent(this, MyGPSService::class.java)
        bindService(bindIntent, serviceConn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConn)
    }
}