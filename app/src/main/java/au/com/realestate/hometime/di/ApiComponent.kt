package au.com.realestate.hometime.di

import au.com.realestate.hometime.network.TramApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: TramApiService)
}