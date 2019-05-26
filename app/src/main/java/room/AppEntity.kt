package room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["city"], unique = true)])
data class AppEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "city")
    var city: String,

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,

    @ColumnInfo(name = "update_time")
    var updateTime: Long,

    var time: Int,

    var summary: String,

    @ColumnInfo(name = "precip_intensity")
    var precipIntensity: Double,

    @ColumnInfo(name = "precip_intensityError")
    var precipIntensityError: Double,

    @ColumnInfo(name = "precip_probability")
    var precipProbability: Double,

    @ColumnInfo(name = "precip_type")
    var precipType: String,

    var temperature: Double,

    @ColumnInfo(name = "apparent_temperature")
    var apparentTemperature: Double,

    var dewPoint: Double,

    var humidity: Double,

    var pressure: Double,

    @ColumnInfo(name = "wind_speed")
    var windSpeed: Double,

    @ColumnInfo(name = "wind_gust")
    var windGust: Double,

    @ColumnInfo(name = "wind_bearing")
    var windBearing: Int,

    @ColumnInfo(name = "cloud_cover")
    var cloudCover: Double,

    var visibility: Double

)
