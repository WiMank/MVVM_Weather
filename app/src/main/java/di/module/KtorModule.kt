package di.module

import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class KtorModule : AnkoLogger {
    val ktorClientModule = Kodein.Module("ktor_client_module") {

        bind<HttpClient>() with singleton {
            HttpClient {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                expectSuccess = true
            }
        }
    }
}