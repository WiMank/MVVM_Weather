package mvvm.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.anko.AnkoLogger
import utils.NetManager

class RepoForecastModel(private val netManager: NetManager) : AnkoLogger {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    /*   fun getForecast() {
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
       }*/
}