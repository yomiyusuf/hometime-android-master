package au.com.realestate.hometime.network

import au.com.realestate.hometime.models.*
import au.com.realestate.hometime.util.Constants.GET_PREDICTED_TIME
import au.com.realestate.hometime.util.Constants.GET_TOKEN
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TramApi {

    @GET(GET_TOKEN)
    fun getApiToken(): Single<ApiResponse<Token>>

    @GET(GET_PREDICTED_TIME)
    fun getPredictedTime(
            @Path("stopId")stop_id: String,
            @Query("token")token: String)
            : Single<ApiResponse<Tram>>
}