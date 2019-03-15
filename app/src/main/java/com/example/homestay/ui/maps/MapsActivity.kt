package com.example.homestay.ui.maps

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.homestay.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONException
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener{

    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.anim_sliding_in_right, R.anim.anim_sliding_out_left)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun initListener() {
        mMap.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        mMap.clear()
        val markerOptions = MarkerOptions()
        mMap.addMarker(markerOptions.position(latLng).title(getAddressName(latLng)))
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun getAddressName(latLng: LatLng): String {
        var placeName = ""
        val geocoder = Geocoder(this@MapsActivity)
        try {
            placeName = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0].getAddressLine(0)
        } catch (e: Exception) {
            Log.d("Location name:", e.toString())
        }
        return placeName
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMaxZoomPreference(20f)
        mMap.setMinZoomPreference(1f)
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        /*val sydney = LatLng(11.571899, 104.916938)
        mMap.setMaxZoomPreference(20f)
        mMap.setMinZoomPreference(1f)
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val cameraPosition = CameraPosition.Builder()
            .target(sydney)
            .zoom(8f)
            .bearing(0f)
            .tilt(0f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))*/
        if (checkLocationPermission()) {
            mMap.isMyLocationEnabled = true
            val mLocationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as (LocationManager)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        }
        initListener()
    }

    /*fun createCustomMarker(context: Context, @DrawableRes resource: Int, _name: String): Bitmap {

        val marker = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custom_marker_layout,
            null
        )

        val markerImage = marker.findViewById(R.id.user_dp) as CircleImageView
        markerImage.setImageResource(resource)
        val tvName = marker.findViewById(R.id.name) as TextView
        tvName.text = _name

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(marker.measuredWidth, marker.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        marker.draw(canvas)

        return bitmap
    }*/

    //Request nearby restaurant LatLng value using Volley
    private fun volleyRequest(latLng: LatLng) {
        val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latLng.latitude + "," + latLng.longitude + "&radius=500&type=hotel&key=AIzaSyA5y_SEMEVimFL9Fi4DlsCy0BLbD7KYFWw")
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(url, null, com.android.volley.Response.Listener<JSONObject> { response ->
            try {
                val results = response.getJSONArray("results")
                if (results.length() > 0) {
                    for (i in 0 until results.length()) {
                        val restaurant = results.getJSONObject(i)
                        val geometry = restaurant.getJSONObject("geometry")
                        val location = geometry.getJSONObject("location")
                        val lat = location.getDouble("lat")
                        val lng = location.getDouble("lng")

                        val markerOptions = MarkerOptions()
                            .title(restaurant.getString("name"))
                            .position(LatLng(lat, lng))
                            .icon(BitmapDescriptorFactory.defaultMarker())
                        mMap.addMarker(markerOptions)
                    }
                }
            } catch (e: JSONException) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }, com.android.volley.Response.ErrorListener {

        })
        queue.add(request)
    }

    override fun onRequestPermissionsResult(requestCode:Int, @NonNull permissions:Array<String>, @NonNull grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION)
        {
            if (checkLocationPermission())
            {
                mMap.isMyLocationEnabled = true
                val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
            }
        }
    }
    private fun checkLocationPermission():Boolean {
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION)
            Toast.makeText(this@MapsActivity, "No access to user permission", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onLocationChanged(location: Location) {
        val mCurrentLocation = LatLng(location.latitude, location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLocation))
        findNearbyHotel(mCurrentLocation)
        /*val cameraPosition = CameraPosition.Builder()
            .target(mCurrentLocation)
            .zoom(8f)
            .bearing(0f)
            .tilt(0f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))*/
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    private fun findNearbyHotel(mCurrentLocation: LatLng) {
        btnNearbyHotel.setOnClickListener{
            volleyRequest(mCurrentLocation)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as (LocationManager)
        locationManager.removeUpdates(this)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_sliding_in_left, R.anim.anim_sliding_out_right)
    }
}
