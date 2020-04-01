package au.com.realestate.hometime.network

import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.PredictedTime
import au.com.realestate.hometime.util.Constants.GET_PREDICTED_TIME
import au.com.realestate.hometime.util.Constants.GET_TOKEN
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TramApi {

    @GET(GET_TOKEN)
    fun getApiToken(): Single<ApiToken>

    @GET(GET_PREDICTED_TIME)
    fun getPredictedTime(
            @Path("stopId")stop_id: String,
            @Query("token")token: String)
            : Single<PredictedTime>
}