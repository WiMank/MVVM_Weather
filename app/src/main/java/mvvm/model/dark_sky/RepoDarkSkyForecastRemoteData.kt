package mvvm.model.dark_sky

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import mvvm.model.RepoPreference
import mvvm.model.gps.GPSCoordinates
import secret.DARK_SKY_API_LINK
import utils.UNITS_PREF_KEY
import utils.W_LANGUAGE

class RepoDarkSkyForecastRemoteData(
    private val ktorClient: HttpClient,
    private val preference: RepoPreference
) {

    suspend fun forecastRemote(coordinates: GPSCoordinates): DarkSkyForecast.DarkSky {
        return ktorClient.get(
            DARK_SKY_API_LINK + "${coordinates.latitude},${coordinates.longitude}?exclude=minutely,alerts,flags&lang=${preference.getStringsSettings(
                W_LANGUAGE
            )}&units=${preference.getStringsSettings(UNITS_PREF_KEY)}"
        )
    }
}
