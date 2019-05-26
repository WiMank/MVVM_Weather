package mvvm.model.gps

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import utils.GPS

class RepoGPSCoordinates(private val mContext: Context) : AnkoLogger, LocationListener {

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onLocationChanged(location: Location?) {
        info("${location?.latitude} ${location?.longitude}")
    }

    suspend fun location(): GPSCoordinates = withContext(Dispatchers.Main) {
        var coordinates = GPSCoordinates(0.0, 0.0)
        var location: Location
        if (checkPermission()) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100L,
                    100f,
                    this@RepoGPSCoordinates
                )
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                coordinates = GPSCoordinates(location.longitude, location.latitude)
                locationManager.removeUpdates(this@RepoGPSCoordinates)
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    100L,
                    100f,
                    this@RepoGPSCoordinates
                )
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                coordinates = GPSCoordinates(location.longitude, location.latitude)
                locationManager.removeUpdates(this@RepoGPSCoordinates)
            }
        }
        coordinates
    }


    @AfterPermissionGranted(GPS)
    private fun checkPermission(): Boolean {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(mContext, *perms)
    }
}