package com.future.tailormade.core.helper

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

class MyLocationManager private constructor(private val context: Context) {

  private val _receivingLocationUpdates = MutableLiveData(false)
  val receivingLocationUpdates: LiveData<Boolean>
    get() = _receivingLocationUpdates

  private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
      context)

  private val locationRequest: LocationRequest = LocationRequest().apply {
    interval = TimeUnit.SECONDS.toMillis(60)
    fastestInterval = TimeUnit.SECONDS.toMillis(30)
    maxWaitTime = TimeUnit.MINUTES.toMillis(2)
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
  }

//  private val locationUpdatePendingIntent: PendingIntent by lazy {
//    val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
//    intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
//    PendingIntent.getBroadcast(context, 0, intent,
//        PendingIntent.FLAG_UPDATE_CURRENT)
//  }

  @RequiresApi(Build.VERSION_CODES.Q) @Throws(SecurityException::class)
  @MainThread fun startLocationUpdates() {
    if (isLocationRequestPermitted().not()) return

    try {
      _receivingLocationUpdates.value = true
//      fusedLocationClient.requestLocationUpdates(locationRequest,
//          locationUpdatePendingIntent)
    } catch (permissionRevoked: SecurityException) {
      _receivingLocationUpdates.value = false
      throw permissionRevoked
    }
  }

  @MainThread
  fun stopLocationUpdates() {
    _receivingLocationUpdates.value = false
//    fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun isLocationRequestPermitted(): Boolean {
    return ActivityCompat.checkSelfPermission(context,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

  companion object {
    @Volatile private var INSTANCE: MyLocationManager? = null

    fun getInstance(context: Context): MyLocationManager {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: MyLocationManager(context).also { INSTANCE = it }
      }
    }
  }
}