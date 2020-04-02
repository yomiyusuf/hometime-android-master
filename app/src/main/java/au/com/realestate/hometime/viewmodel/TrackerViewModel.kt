package au.com.realestate.hometime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramApiService
import au.com.realestate.hometime.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class TrackerViewModel(application: Application) : AndroidViewModel(application) {
    val trams by lazy { MutableLiveData<List<Tram>>() }
    val loadingError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val prefs = SharedPreferencesHelper(application)
    private val disposable = CompositeDisposable()
    private val apiService = TramApiService()

    private lateinit var _stops: List<String>
    private var invalidApiToken = false

    fun refresh(stops: List<String>) {
        isLoading()
        invalidApiToken = false
        _stops = stops
        val token = prefs.apiToken
        if (token.isNullOrEmpty()){
            getToken()
        } else {
            getPredictedTimes(token = token)
        }
    }

    private fun getToken() {
        disposable.add(
                apiService.getApiToken()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<ApiResponse<ApiToken>>() {
                            override fun onSuccess(response: ApiResponse<ApiToken>) {
                                if (response.hasError) {
                                    loadingError.value = true
                                    loadingFinished()
                                } else {
                                    val tkn = response.responseObject[0].toString()
                                    prefs.apiToken = tkn
                                    getPredictedTimes(token = tkn)
                                }
                            }

                            override fun onError(e: Throwable) {
                                loadingFinished()
                                loadingError.value = true
                            }
                        })
        )
    }

    private fun getPredictedTimes(token: String) {
        _stops.forEach {
            getPredictedTimes(it, token)
        }
    }

    private fun getPredictedTimes(stopId: String, token: String) {
        disposable.add(
                apiService.getPredictedTimes(stopId, token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<ApiResponse<Tram>>() {
                            override fun onSuccess(response: ApiResponse<Tram>) {

                                if (response.hasError) {
                                    loadingError.value = true
                                    loadingFinished()
                                } else {
                                    val tramList = response.responseObject
                                    loadingFinished()
                                    trams.value = tramList
                                }
                            }

                            //In case of error, get new token and try again.
                            // Try again just once to avoid endless loop
                            override fun onError(e: Throwable) {
                                if(!invalidApiToken) {
                                    invalidApiToken = true
                                    getToken()
                                } else {
                                    e.printStackTrace()
                                    loadingError.value = true
                                    loadingFinished()
                                }
                            }
                        })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun loadingFinished() {
        loading.value = false
    }

    private fun isLoading() {
        loading.value = true
    }
}