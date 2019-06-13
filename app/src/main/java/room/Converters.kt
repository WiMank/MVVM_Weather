package room

import androidx.room.TypeConverter
import com.google.gson.Gson
import mvvm.model.dark_sky.DarkSkyForecast
import org.jetbrains.anko.AnkoLogger

class Converters : AnkoLogger {

    @TypeConverter
    fun listToJson(value: DarkSkyForecast.Daily): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): DarkSkyForecast.Daily {
        return Gson().fromJson(value, DarkSkyForecast.Daily::class.java)
    }
}