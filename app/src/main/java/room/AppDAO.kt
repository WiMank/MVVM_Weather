package room

import androidx.room.*

@Dao
interface AppDAO {

    @Query("SELECT * FROM  appEntity")
    suspend fun getAll(): List<AppEntity>

    @Query("SELECT * FROM appEntity WHERE id = :id")
    suspend fun getById(id: Long): AppEntity

    @Query("SELECT * FROM appEntity WHERE city = :name")
    suspend fun getByNameAsync(name: String): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(previewPositionEntity: AppEntity)

    @Query("SELECT (update_time) FROM appEntity WHERE city == :qCity ")
    suspend fun updateTime(qCity: String): Long

    @Update
    suspend fun update(value: AppEntity)

    @Delete
    suspend fun delete(value: AppEntity)

}