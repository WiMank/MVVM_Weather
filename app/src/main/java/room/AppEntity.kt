package room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import mvvm.model.dark_sky.DarkSkyForecast

@Entity(indices = [Index(value = ["city"], unique = true)])
data class AppEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,

    @ColumnInfo(name = "update_time")
    var updateTime: Long = 0,

    var time: Long = 0,

    var summary: String = "",

    @ColumnInfo(name = "precip_probability")
    var precipProbability: Double = 0.0,

    @ColumnInfo(name = "precip_type")
    var precipType: String? = "",

    var temperature: Double = 0.0,

    @ColumnInfo(name = "apparent_temperature")
    var apparentTemperature: Double = 0.0,

    var humidity: Double = 0.0,

    var pressure: Double = 0.0,

    @ColumnInfo(name = "wind_speed")
    var windSpeed: Double = 0.0,

    @ColumnInfo(name = "wind_gust")
    var windGust: Double = 0.0,

    @ColumnInfo(name = "cloud_cover")
    var cloudCover: Double = 0.0,

    var visibility: Double = 0.0,

    var icon: String = "",

    @ColumnInfo(name = "json_daily_array")
    var jsonArray: DarkSkyForecast.Daily
)