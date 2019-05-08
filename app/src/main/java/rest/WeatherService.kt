package rest

import rest.pojo.DarkSkyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("/forecast/{key}/{latitude},{longitude}")
    fun weatherProvider(
        @Path("key") key: String,
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String,
        @Query("exclude") excludeForecast: String
    ): Call<DarkSkyModel.DarkSky>
}