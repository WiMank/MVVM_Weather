package di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import rest.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitKodeinModule {

    val retrofitModule = Kodein.Module("retrofit_module") {

        import(OkHttpKodeinModule().okHttpModule)

        bind<WeatherService>() with singleton {
            Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .client(instance())
                .addConverterFactory(instance())
                .build()
                .create(WeatherService::class.java)
        }

        bind<Gson>() with singleton { GsonBuilder().create() }

        bind<GsonConverterFactory>() with singleton { GsonConverterFactory.create(instance()) }
    }
}