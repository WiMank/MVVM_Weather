package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityEntity: CityEntity)

    @Query("SELECT * FROM city_query WHERE id = 0")
    fun getCityName(): CityEntity?
}