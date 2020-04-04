package au.com.realestate.hometime.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import au.com.realestate.hometime.di.ApiModuleTest
import au.com.realestate.hometime.di.AppModule
import au.com.realestate.hometime.di.DaggerViewModelComponent
import au.com.realestate.hometime.di.PrefsModuleTest
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramApiService
import au.com.realestate.hometime.util.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class TrackerViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var tramApiService: TramApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    val application = Mockito.mock(Application::class.java)
    var trackerViewModel = TrackerViewModel(application, true)
    private val apiToken = "test apiToken"

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
                .appModule(AppModule(application))
                .apiModule(ApiModuleTest(tramApiService))
                .prefsModule(PrefsModuleTest(prefs))
                .build()
                .inject(trackerViewModel)
    }

    @Test
    fun getPredictedTimesSuccess() {
        Mockito.`when`(prefs.apiToken).thenReturn(apiToken)
        val tram = Tram("Richmond", "/Date(1585964874000+1100)/", "78")
        val response = ApiResponse<Tram> (null, hasError = false, hasResponse = true, responseObject = listOf(tram))
        val stopId = "4000"

        val testSingle = Single.just(response)

        Mockito.`when`(tramApiService.getPredictedTimes(apiToken, stopId)).thenReturn(testSingle)

        trackerViewModel.refresh(listOf(stopId))
        Assert.assertEquals(1, trackerViewModel.trams.value?.size)
        Assert.assertEquals(false, trackerViewModel.loadingError.value)
        Assert.assertEquals(false, trackerViewModel.loading.value)
    }

    @Test
    fun getPredictedTimesFailure() {
        Mockito.`when`(prefs.apiToken).thenReturn(apiToken)
        val testSingle = Single.error<ApiResponse<Tram>>(Throwable())
        val stopId = "4000"

        val token = ApiToken("valid apiToken")
        val tokenSingle = Single.just(ApiResponse<ApiToken>(
                null, hasError = false, hasResponse = true, responseObject = listOf(token)))

        Mockito.`when`(tramApiService.getPredictedTimes(apiToken, stopId)).thenReturn(testSingle)
        Mockito.`when`(tramApiService.getApiToken()).thenReturn(tokenSingle)

        trackerViewModel.refresh(listOf(stopId))

    }
}