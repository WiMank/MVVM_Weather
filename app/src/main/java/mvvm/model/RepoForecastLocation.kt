package mvvm.model

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import utils.GPS

class RepoForecastLocation(private val mContext: Context) : AnkoLogger,
    LocationListener {
    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onLocationChanged(location: Location?) {
        info("${location?.latitude} ${location?.longitude}")
    }

    private lateinit var location: Location

    fun location(): Coordinates {
        var coordinates = Coordinates(0.0, 0.0)
        if (checkPermission()) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 100f, this)
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                coordinates = Coordinates(
                    location.longitude,
                    location.latitude
                )
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100L, 100f, this)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                coordinates = Coordinates(
                    location.longitude,
                    location.latitude
                )
            }
        }
        return coordinates
    }


    @AfterPermissionGranted(GPS)
    private fun checkPermission(): Boolean {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(mContext, *perms)
    }
}