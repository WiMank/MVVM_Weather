package mvvm.model

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import rest.pojo.DarkSkyPojo
import room.AppDAO
import room.AppEntity
import secret.DARK_SKY_API_LINK

private const val ONE_HOUR = 3600000L

class RepoForecastRemoteData(private val appDAO: AppDAO, private val ktorClient: HttpClient) {
    private val currentTime = System.currentTimeMillis()

    suspend fun forecastRemoteAsync(coordinates: Coordinates): DarkSkyPojo.DarkSky? {

        val forecast: DarkSkyPojo.DarkSky = ktorClient.use {
            it.get(
                DARK_SKY_API_LINK + "56ujfy${coordinates.latitude},${coordinates.longitude}"
            )
        }

        appDAO.insert(
            AppEntity(0, "TEST", coordinates.latitude, coordinates.longitude, currentTime + ONE_HOUR)
        )
        return forecast
    }
}