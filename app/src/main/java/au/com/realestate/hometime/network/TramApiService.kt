package au.com.realestate.hometime.network

import au.com.realestate.hometime.di.DaggerApiComponent
import au.com.realestate.hometime.models.*
import io.reactivex.Single
import javax.inject.Inject

class TramApiService {

    @Inject
    lateinit var api: TramApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getApiToken(): Single<ApiResponse<ApiToken>> {
        return api.getApiToken()
    }

    fun getPredictedTimes(stopId: String, token: String): Single<ApiResponse<Tram>> {
        return api.getPredictedTime(stopId, token)
    }
}