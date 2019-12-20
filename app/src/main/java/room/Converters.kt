package room

import androidx.room.TypeConverter
import com.google.gson.Gson
import mvvm.model.dark_sky.DarkSkyForecast
import org.jetbrains.anko.AnkoLogger

class Converters : AnkoLogger {

    @TypeConverter
    fun listDailyToJson(value: DarkSkyForecast.Daily): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonDailyToList(value: String): DarkSkyForecast.Daily {
        return Gson().fromJson(value, DarkSkyForecast.Daily::class.java)
    }


    @TypeConverter
    fun listHourlyToJson(value: DarkSkyForecast.Hourly): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonHourlyToList(value: String): DarkSkyForecast.Hourly {
        return Gson().fromJson(value, DarkSkyForecast.Hourly::class.java)
    }
}
