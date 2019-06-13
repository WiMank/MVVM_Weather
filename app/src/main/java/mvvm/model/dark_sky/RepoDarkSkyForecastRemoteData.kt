package mvvm.model.dark_sky

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import mvvm.model.gps.GPSCoordinates
import secret.DARK_SKY_API_LINK


class RepoDarkSkyForecastRemoteData(private val ktorClient: HttpClient) {

    suspend fun forecastRemote(coordinates: GPSCoordinates): DarkSkyForecast.DarkSky {
        return ktorClient.get(
            //TODO: Выбор языка и выбор едениц измерения через настройки
            DARK_SKY_API_LINK + "${coordinates.latitude},${coordinates.longitude}?exclude=minutely,alerts,flags&lang=ru&units=si"
            )
        }
    }