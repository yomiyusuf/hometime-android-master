package au.com.realestate.hometime.network

import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.PredictedTime
import au.com.realestate.hometime.util.Constants.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TramApiService {

    private  val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TramApi::class.java)

    fun getApiToken(): Single<ApiToken> {
        return api.getApiToken()
    }

    fun getPredictedTime(stopId: String, token: String): Single<PredictedTime> {
        return api.getPredictedTime(stopId, token)
    }
}