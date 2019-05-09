package di.module

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

        bind() from singleton {
            Retrofit.Builder()
                .baseUrl("https://api.darksky.net")
                .client(instance())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherService::class.java)
        }
    }
}