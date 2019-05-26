package di.module

import androidx.room.Room
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import room.AppDataBase

class RoomModule {

    val roomModule = Kodein.Module("room_module") {
        bind() from eagerSingleton {
            Room.databaseBuilder(instance(), AppDataBase::class.java, "weather_database.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        bind() from eagerSingleton { instance<AppDataBase>().appDAO() }
    }
}