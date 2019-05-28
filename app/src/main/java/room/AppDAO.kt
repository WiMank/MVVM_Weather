package room

import androidx.room.*

@Dao
interface AppDAO {

    @Query("SELECT * FROM  appEntity")
    suspend fun getAll(): List<AppEntity>

    @Query("SELECT * FROM appEntity WHERE id = :id")
    suspend fun getById(id: Long): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(previewPositionEntity: AppEntity)

    @Query("SELECT * FROM appEntity WHERE city == :qCity AND :time <= update_time")
    fun hasNeedUpdate(qCity: String, time: Long): Boolean

    @Update
    suspend fun update(previewPositionEntity: AppEntity)

    @Delete
    suspend fun delete(previewPositionEntity: AppEntity)

}