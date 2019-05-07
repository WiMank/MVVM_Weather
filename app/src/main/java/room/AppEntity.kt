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
    var searchQuery: String,

    @ColumnInfo(name = "update_time")
    var updateTime: Long

)
