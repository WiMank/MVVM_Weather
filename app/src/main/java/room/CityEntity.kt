package room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_query")
data class CityEntity(

    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "city_name")
    var cityName: String = "Moscow"
)
