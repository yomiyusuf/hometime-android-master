package au.com.realestate.hometime.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class TrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()
    private val apiService = TramApiService()

    private lateinit var _stops: List<String>

    fun refresh(stops: List<String>) {
        _stops = stops
        getToken()
    }

    private fun getToken() {
        disposable.add(
                apiService.getApiToken()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<ApiResponse<ApiToken>>() {
                            override fun onSuccess(t: ApiResponse<ApiToken>) {
                                Log.e("TrackerViewModel", t.responseObject[0].toString())
                                _stops.forEach {
                                    getPredictedTimes(it, t.responseObject[0].deviceToken)
                                }
                            }

                            override fun onError(e: Throwable) {
                                Log.e("TrackerViewModel", e.message)
                            }

                        })
        )
    }

    private fun getPredictedTimes(stopId: String, token: String) {
        disposable.add(
                apiService.getPredictedTimes(stopId, token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<ApiResponse<Tram>>() {
                            override fun onSuccess(t: ApiResponse<Tram>) {
                                Log.e("TrackerViewModel", t.responseObject[0].toString())
                            }

                            override fun onError(e: Throwable) {
                                Log.e("TrackerViewModel", e.message)
                            }

                        })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}