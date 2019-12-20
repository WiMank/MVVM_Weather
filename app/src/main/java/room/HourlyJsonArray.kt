package room

import androidx.room.ColumnInfo
import mvvm.model.dark_sky.DarkSkyForecast

@Suppress("unused")
data class HourlyJsonArray(
    @ColumnInfo(name = "json_hourly_array")
    var jsonArray: DarkSkyForecast.Hourly
)
