package mvvm.model.mapBox

import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.geojson.Point
import mvvm.model.gps.GPSCoordinates
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import secret.MAP_BOX_TOKEN

class RepoMapBox : AnkoLogger {

    fun getLocationName(gpsCoordinates: GPSCoordinates): String {

        info("TEST BOX latitude: ${gpsCoordinates.latitude}  longitude: ${gpsCoordinates.longitude}")

        val map = MapboxGeocoding.builder()
            .accessToken(MAP_BOX_TOKEN)
            .query(Point.fromLngLat(gpsCoordinates.longitude, gpsCoordinates.latitude))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
            .build().executeCall()

        return if (map.isSuccessful and map.body()!!.features().isNotEmpty()) {
            map.body()?.features()!![0].text().toString()
        } else {
            "Empty"
        }
    }
}