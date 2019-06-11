package room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AppEntity::class, CityEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
    abstract fun cityDAO(): CityDAO
}