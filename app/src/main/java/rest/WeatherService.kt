package rest

import rest.pojo.Forecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/customsearch/v1")
    fun weatherPtovider(
        @Query("key") key: String,
        @Query("fields") fields: String,
        @Query("cx") cx: String,
        @Query("q") query: String
    ): Call<Forecast>
}