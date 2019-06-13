package room

import androidx.room.ColumnInfo
import mvvm.model.dark_sky.DarkSkyForecast

@Suppress("unused")
data class JsonArray(
    @ColumnInfo(name = "json_daily_array")
    var jsonArray: DarkSkyForecast.DataItem
)