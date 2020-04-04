package au.com.realestate.hometime.di

import au.com.realestate.hometime.viewmodel.TrackerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent {

    fun inject(viewModel: TrackerViewModel)
}