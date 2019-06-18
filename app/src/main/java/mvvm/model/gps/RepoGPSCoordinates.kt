package mvvm.model.gps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import utils.GPS

class RepoGPSCoordinates(private val context: Context) : AnkoLogger {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val client: SettingsClient = LocationServices.getSettingsClient(context)
    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var locationChannel: Channel<GPSCoordinates>

    fun getLocation(): Channel<GPSCoordinates> {
        if (checkPermission()) {
            locationChannel = Channel()
            val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            client.checkLocationSettings(locationSettingsRequest).apply {
                addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
                addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        startActivity(
                            context,
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            null
                        )
                        context.toast(R.string.location_enable)
                    }
                }
            }
        }
        return locationChannel
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private var locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                scope.launch {
                    info { "LOCATION SEND ${location.longitude}, ${location.latitude}" }
                    locationChannel.send(GPSCoordinates(location.longitude, location.latitude))
                    locationChannel.close()
                    stopLocationUpdates()
                }
            }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            if (!locationAvailability.isLocationAvailable) {
                locationChannel.close()
                stopLocationUpdates()
            }
        }
    }

    private var locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @AfterPermissionGranted(GPS)
    private fun checkPermission(): Boolean {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(context, *perms)
    }
}