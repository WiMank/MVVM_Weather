package room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AppEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
}