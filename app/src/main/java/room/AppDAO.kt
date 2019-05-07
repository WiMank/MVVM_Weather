package room

import androidx.room.*

@Dao
interface AppDAO {

    @Query("SELECT * FROM  appEntity")
    fun getAll(): List<AppEntity>

    @Query("SELECT * FROM appEntity WHERE id = :id")
    fun getById(id: Long): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(previewPositionEntity: AppEntity)

    @Update
    fun update(previewPositionEntity: AppEntity)

    @Delete
    fun delete(previewPositionEntity: AppEntity)

}