package room

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface AppDAO {

    @Query("SELECT * FROM  appEntity")
    suspend fun getAll(): List<AppEntity>

    @Query("SELECT * FROM appEntity WHERE id = :id")
    suspend fun getById(id: Long): AppEntity

    @Query("SELECT * FROM appEntity WHERE city = :name")
    fun getByNameAsync(name: String): Flowable<AppEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(previewPositionEntity: AppEntity)

    @Query("SELECT (update_time) FROM appEntity WHERE city == :qCity ")
    suspend fun updateTime(qCity: String): Long?

    @Update
    suspend fun update(value: AppEntity)

    @Delete
    suspend fun delete(value: AppEntity)

}