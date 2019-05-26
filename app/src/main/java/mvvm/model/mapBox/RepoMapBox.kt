package mvvm.model.mapBox

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import mvvm.model.gps.GPSCoordinates
import secret.MAP_BOX_BASE_LINK
import secret.MAP_BOX_BASE_TOKEN

class RepoMapBox(private val ktorClient: HttpClient) {

    suspend fun getLocationName(gpsCoordinates: GPSCoordinates): Response {
        return ktorClient.use {
            it.get(
                "$MAP_BOX_BASE_LINK${gpsCoordinates.longitude},${gpsCoordinates.latitude}$MAP_BOX_BASE_TOKEN.json"
            )
        }
    }
}