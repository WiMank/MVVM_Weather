package mvvm.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import rest.WeatherService
import rest.pojo.DarkSkyPojo
import retrofit2.Response
import secret.darkSkyApiKey

class RepoForecastModel(private val mWeatherService: WeatherService) : AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    fun getForecast() {
        scope.launch {
            val response: Response<DarkSkyPojo.DarkSky> =
                mWeatherService.weatherProvider(
                    darkSkyApiKey,
                    "55.75222",
                    "37.61556",
                    "daily"
                ).execute()
            val body = response.body()
            info { body }
        }
    }
}