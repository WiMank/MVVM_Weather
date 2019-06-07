package mvvm.model.mapBox

import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.geojson.Point
import mvvm.model.gps.GPSCoordinates
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import secret.MAP_BOX_TOKEN


class RepoMapBox : AnkoLogger {

    fun locationName(gpsCoordinates: GPSCoordinates): String {

        info("MAP BOX latitude: ${gpsCoordinates.latitude}  longitude: ${gpsCoordinates.longitude}")

        val map = MapboxGeocoding.builder()
            .accessToken(MAP_BOX_TOKEN)
            .query(Point.fromLngLat(gpsCoordinates.longitude, gpsCoordinates.latitude))
            .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
            .build().executeCall()

        return if (map.isSuccessful and map.body()!!.features().isNotEmpty())
            map.body()?.features()!![0].text().toString()
        else "EMPTY"
    }


    fun coordinatesByName(place: String): GPSCoordinates {

        val mapBoxGeocoding = MapboxGeocoding.builder()
            .accessToken(MAP_BOX_TOKEN)
            .query(place)
            .build().executeCall()

        return if (mapBoxGeocoding.isSuccessful and mapBoxGeocoding.body()?.features()!!.isNotEmpty())
            GPSCoordinates(
                mapBoxGeocoding.body()!!.features()[0].center()!!.longitude(),
                mapBoxGeocoding.body()!!.features()[0].center()!!.latitude()
            )
        else GPSCoordinates()
    }
}