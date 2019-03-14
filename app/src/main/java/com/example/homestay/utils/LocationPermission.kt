package com.example.homestay.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.widget.Toast

object LocationPermission {
    private val REQUEST_LOCATION: Int = 6
    fun checkLocationPermission(activity: Activity):Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity.baseContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION)
            Toast.makeText(activity.baseContext, "No access to user permission", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}