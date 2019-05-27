package mvvm.model.mapBox

import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.geojson.Point
import mvvm.model.gps.GPSCoordinates
import secret.MAP_BOX_TOKEN

class RepoMapBox {

    fun getLocationName(gpsCoordinates: GPSCoordinates): String? {
        val map = MapboxGeocoding.builder()
            .accessToken(MAP_BOX_TOKEN)
            .query(Point.fromLngLat(gpsCoordinates.longitude, gpsCoordinates.latitude))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
            .build().executeCall()
        return map.body()?.features()!![0].text()
    }
}