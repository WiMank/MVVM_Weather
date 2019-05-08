package ui.activity

import android.content.SharedPreferences
import android.os.Bundle
import com.wimank.mvvm.weather.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.anko.info
import org.kodein.di.generic.instance
import rest.WeatherService
import rest.pojo.DarkSkyModel
import retrofit2.Response
import secret.darkSkyApiKey

class MainActivity : KodeinActivity() {

    private val mSharedPreferences: SharedPreferences by instance()
    private val mWeatherService: WeatherService by instance()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSharedPreferences.edit().putString("hhh", "").apply()


        scope.launch {
            val response: Response<DarkSkyModel.DarkSky> =
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
