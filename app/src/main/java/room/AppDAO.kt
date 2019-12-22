package room

import androidx.room.*
import mvvm.model.dark_sky.DarkSkyForecast

@Dao
interface AppDAO {

    @Query("SELECT * FROM appEntity WHERE id = :id")
    suspend fun getById(id: Long): AppEntity

    @Query("SELECT * FROM appEntity WHERE city = :name")
    suspend fun getByNameAsync(name: String): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(previewPositionEntity: AppEntity)

    @Query("SELECT (update_time) FROM appEntity WHERE city == :qCity ")
    suspend fun updateTime(qCity: String): Long?

    @Update
    suspend fun update(value: AppEntity)

    @Delete
    suspend fun delete(value: AppEntity)

    @Query("SELECT json_daily_array FROM appEntity WHERE city == :searchQueryInDb")
    suspend fun loadDailyJsonArrayFromDb(searchQueryInDb: String): DarkSkyForecast.Daily

    @Query("SELECT json_hourly_array FROM appEntity WHERE city == :searchQueryInDb")
    suspend fun loadHourlyJsonArrayFromDb(searchQueryInDb: String): DarkSkyForecast.Daily

}
