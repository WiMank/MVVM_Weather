package rest

import kotlinx.coroutines.Deferred
import rest.pojo.DarkSkyPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("/forecast/{key}/{latitude},{longitude}")
    fun weatherProviderAsync(
        @Path("key") key: String,
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String,
        @Query("exclude") excludeForecast: String
    ): Deferred<Response<DarkSkyPojo.DarkSky>>
}