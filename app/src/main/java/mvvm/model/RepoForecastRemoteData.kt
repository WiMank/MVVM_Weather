package mvvm.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import rest.WeatherService
import rest.pojo.DarkSkyPojo
import retrofit2.Response
import room.AppDAO
import room.AppEntity
import secret.DARK_SKY_API_KEY

private const val ONE_HOUR = 3600000L

class RepoForecastRemoteData(
    private val appDAO: AppDAO,
    private val weatherService: WeatherService,
    private val coordinates: Coordinates
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val currentTime = System.currentTimeMillis()


    private fun getData(): DarkSkyPojo.DarkSky? {
        val response: Response<DarkSkyPojo.DarkSky> =
            weatherService.weatherProvider(
                DARK_SKY_API_KEY,
                coordinates.latitude.toString(),
                coordinates.toString(),
                "daily"
            ).execute()

        if (response.isSuccessful) {
            appDAO.insert(
                AppEntity(0, "TEST", coordinates.latitude, coordinates.longitude, currentTime + ONE_HOUR)
            )
        }
        return response.body()
    }
}