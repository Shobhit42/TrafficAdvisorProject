package com.example.trafficmanagementsystem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.activity_route.lottie
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class RouteActivity : AppCompatActivity() {

    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var sharedPref : SharedPreferences

    @field:Named("Image")
    @JvmField
    @Inject
    var base64String = ""

    @field:Named("Name")
    @JvmField
    @Inject
    var userNickName = ""

    @SuppressLint("TimberArgCount")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        if(ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION ),111)
        }

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            // Set the map type to normal
                map = it
        }

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        if(isLocationEnabled()){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){task->
                val location: Location?= task.result
                if(location==null){
                    Toast.makeText(this , "Location Null" , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this , "Success" , Toast.LENGTH_SHORT).show()
                    tv_Latitude.text = ("Latitude : ${location.latitude}")
                    tv_Longitude.text = ("Longitude : ${location.longitude}")

                    val latitude = location.latitude
                    val longitude = location.longitude
                    val zoomLevel = 17f

                    // Create a LatLng object from the user's location
                    val latLng = LatLng(latitude, longitude)

                    map?.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("Marker Title"))

                    // Move the camera to the user's location and set the zoom level
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                }
            }
        }else{
            Toast.makeText(this , "Turn On Location" , Toast.LENGTH_SHORT).show()
        }

        lottie.animate()

        val base64String = sharedPref.getString(Constants.KEY_IMG, "");
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        profile2.setImageBitmap(bitmap)

        userName.text = sharedPref.getString(Constants.KEY_NAME, "")

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = ContextCompat.getColor(this , R.color.purple_200)

        btnToggleStart.setOnClickListener {
            val intent = Intent(this , MainActivity::class.java);
            startActivity(intent);
        }

        iv_add.setOnClickListener {
            var city:String = ed_destination_input.text.toString()
            var gc = Geocoder(this, Locale.getDefault())
            var addresses = gc.getFromLocationName(city,2)
            var address = addresses?.get(0)
            if (address != null) {
                tv_Latitude_destination.text = ("Latitude : ${address.latitude}")
                tv_Longitude_Destination.text = ("Longitude : ${address.longitude}")
            }
        }
    }

    private fun isLocationEnabled() : Boolean {

        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER ) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
        }
    }
}