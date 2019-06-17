package mvvm.model.dark_sky

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import mvvm.model.gps.GPSCoordinates
import secret.DARK_SKY_API_LINK
import utils.Settings
import utils.UNITS_PREF_KEY
import utils.W_LANGUAGE


class RepoDarkSkyForecastRemoteData(private val ktorClient: HttpClient, private val settings: Settings) {

    suspend fun forecastRemote(coordinates: GPSCoordinates): DarkSkyForecast.DarkSky {
        return ktorClient.get(
            //TODO: Выбор языка и выбор едениц измерения через настройки
            DARK_SKY_API_LINK + "${coordinates.latitude},${coordinates.longitude}?exclude=minutely,alerts,flags&lang=${settings.getStringsSettings(
                W_LANGUAGE
            )}&units=${settings.getStringsSettings(UNITS_PREF_KEY)}"
            )
        }
    }