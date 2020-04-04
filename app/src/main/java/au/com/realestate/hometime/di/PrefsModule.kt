package au.com.realestate.hometime.di

import android.app.Application
import au.com.realestate.hometime.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    open fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(app)
    }
}