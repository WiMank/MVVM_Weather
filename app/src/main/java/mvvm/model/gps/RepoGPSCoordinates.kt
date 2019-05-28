package mvvm.model.gps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import utils.GPS

class RepoGPSCoordinates(private val context: Context) {
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val client: SettingsClient = LocationServices.getSettingsClient(context)
    private val locationChannel = Channel<GPSCoordinates>()

    suspend fun getLocation(): Channel<GPSCoordinates> = withContext(Dispatchers.Main) {
        if (checkPermission()) {
            locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        launch {
                            locationChannel.send(GPSCoordinates(location.longitude, location.latitude))
                            locationChannel.close()
                        }
                        stopLocationUpdates()
                    }
                }
            }

            task.addOnSuccessListener { locationCallback }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    startActivity(
                        context,
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        null
                    )
                    context.toast("Необходимо включить местоположение по всем источникам!")
                }
            }
        }
        locationChannel
    }

    fun stopLocationUpdates() {
        if (::locationCallback.isInitialized)
            fusedLocationClient.removeLocationUpdates(locationCallback)
        locationChannel.close()
    }

    @AfterPermissionGranted(GPS)
    private fun checkPermission(): Boolean {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(context, *perms)
    }
}