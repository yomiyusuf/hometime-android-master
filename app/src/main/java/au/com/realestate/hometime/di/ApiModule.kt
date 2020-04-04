package au.com.realestate.hometime.di

import au.com.realestate.hometime.network.TramApi
import au.com.realestate.hometime.network.TramApiService
import au.com.realestate.hometime.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    @Provides
    fun provideTramApi() : TramApi {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TramApi::class.java)
    }

    @Provides
    open fun provideTramApiService(): TramApiService {
        return TramApiService()
    }
}