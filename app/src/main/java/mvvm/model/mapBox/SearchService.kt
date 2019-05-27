package mvvm.model.mapBox

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created 20:55 on 28.12.2018.
 */
interface SearchService {
    @GET("/geocoding/v5/mapbox.places/{longitude},{latitude}.json")
    fun searchSites(
        @Path("longitude") longitude: Double,
        @Path("latitude") latitude: Double,
        @Query("types") types: String,
        @Query("access_token") accessToken: String
    ): Call<MapBox.Response>
}