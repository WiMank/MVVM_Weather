package di.module

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

class OkHttpKodeinModule {

    val okHttpModule = Kodein.Module("ok_http_module") {

        bind() from singleton {
            OkHttpClient()
                .newBuilder()
                .addInterceptor(instance())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        bind() from singleton {
            HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }
        }
    }
}