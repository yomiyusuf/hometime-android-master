package au.com.realestate.hometime.di

import au.com.realestate.hometime.network.TramApiService

class ApiModuleTest(val mockService: TramApiService): ApiModule() {

    override fun provideTramApiService(): TramApiService {
        return mockService
    }
}