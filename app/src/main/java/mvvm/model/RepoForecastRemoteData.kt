package mvvm.model

import rest.WeatherService
import rest.pojo.DarkSkyPojo
import retrofit2.Response
import room.AppDAO
import room.AppEntity
import secret.DARK_SKY_API_KEY

private const val ONE_HOUR = 3600000L

class RepoForecastRemoteData(private val appDAO: AppDAO, private val weatherService: WeatherService) {
    private val currentTime = System.currentTimeMillis()

    suspend fun forecastRemoteAsync(coordinates: Coordinates): DarkSkyPojo.DarkSky? {
        val response: Response<DarkSkyPojo.DarkSky> =
            weatherService.weatherProviderAsync(
                DARK_SKY_API_KEY,
                coordinates.latitude.toString(),
                coordinates.longitude.toString(),
                "daily"
            ).await()

        if (response.isSuccessful) {
            appDAO.insert(
                AppEntity(0, "TEST", coordinates.latitude, coordinates.longitude, currentTime + ONE_HOUR)
            )
        }
        return response.body()
    }
}