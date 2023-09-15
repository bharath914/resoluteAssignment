package com.bharath.resoluteaiassignment.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

lateinit var locationCallback: LocationCallback
lateinit var locationProvider: FusedLocationProviderClient
const val TAG = "Location_TAG"

@SuppressLint("MissingPermission")
@Composable
fun LocationCurrent(context: Context): LocationData {

    val profileViewModel :ProfileViewModel = hiltViewModel()

    locationProvider = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf(LocationData()) }


//    profileViewModel.setLoadingLocation(true)
    DisposableEffect(key1 = locationProvider) {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    currentLocation = LocationData(location.latitude, location.longitude)
                    profileViewModel.setLoadingLocation(false)
                        Toast.makeText(context, "Getting Location", Toast.LENGTH_SHORT).show()
                    break

                }

//                locationProvider.lastLocation
//                    .addOnSuccessListener {location->
//                    location?.let {
//                        Log.d(TAG, "onLocationResult: ")
//                        currentLocation = LocationData(it.latitude,it.longitude)
//                        profileViewModel.setLoadingLocation()
//
//
//                        onDispose {
//                            stopLocationUpdate()
//                        }
//                    }
//
//
//                    }
//                    .addOnFailureListener {
//                        Log.e(TAG, "onLocationResult:${it.message} " )
//                    }
            }


        }
        if (hasPermissions(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )){
            updateLocation()
        }
        else{
            Toast.makeText(context, "Please Grant The Permission", Toast.LENGTH_SHORT).show()
        }

        onDispose {
            stopLocationUpdate()
        }
    }



    return currentLocation

}

fun stopLocationUpdate() {
    try {

        val removeTask  = locationProvider.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener {task->

            if (task.isSuccessful){
                Log.d(TAG, "stopLocationUpdate: removed Location ")
            }else{
                Log.d(TAG, "stopLocationUpdate: failed to remove location callback")
            }
        }


    }catch (    se:SecurityException){
        Log.d("Location_TAG", "stopLocationUpdate: Failed to remove Location Callback .. ${se}")
    }
}


fun updateLocation() {
    locationCallback.let {
        val locationRequest :LocationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.SECONDS.toMillis(30)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        }
        try {


            locationProvider.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
            }catch (e:SecurityException){
                e.printStackTrace()
            }
    }
}

fun hasPermissions(context: Context, vararg permissions: String) =
    permissions.all { permission ->
        ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


fun getReadableLocation(latitude :Double,longitude :Double,context: Context):String{
    var addressText = ""
    val geoCoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses  = geoCoder.getFromLocation(latitude,longitude,1)
        if (addresses?.isNotEmpty()==true){
            val address = addresses[0]
            addressText = "${address.locality}"
            Log.d("GEO", "getReadableLocation: $addressText ")
        }
    }catch (e:IOException){
        Log.d("GEO", "getReadableLocation: ${e.message.toString()} ")
    }
    return addressText
}