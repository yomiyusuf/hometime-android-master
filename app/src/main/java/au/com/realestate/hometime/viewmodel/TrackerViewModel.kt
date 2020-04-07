package au.com.realestate.hometime.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import au.com.realestate.hometime.di.AppModule
import au.com.realestate.hometime.di.DaggerViewModelComponent
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.ApiToken
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramApiService
import au.com.realestate.hometime.util.Constants
import au.com.realestate.hometime.util.SharedPreferencesHelper
import au.com.realestate.hometime.view.model.ErrorStatus
import au.com.realestate.hometime.view.model.ErrorType
import au.com.realestate.hometime.view.model.TramsListModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true): this(application) {
        injected = test
    }
    val trams by lazy { MutableLiveData<MutableList<TramsListModel>>() }
    val loadingError by lazy { MutableLiveData<ErrorStatus>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()
    private lateinit var _stops: List<String>
    private var invalidApiToken = false
    private var injected = false //Flag to prevent real injection when in test case

    @Inject
    lateinit var prefs: SharedPreferencesHelper

    @Inject
    lateinit var apiService: TramApiService

    fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                    .appModule(AppModule(getApplication()))
                    .build()
                    .inject(this)
        }
    }

    fun refresh(stops: List<String>) {
        inject()
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

    fun hardRefresh(stops: List<String>) {
        inject()
        _stops = stops
        isLoading()
        getToken()
    }

    private fun getToken() {
        disposable.add(
                apiService.getApiToken()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<ApiResponse<ApiToken>>() {
                            override fun onSuccess(response: ApiResponse<ApiToken>) {
                                if (response.hasError) {
                                    loadingError.value = ErrorStatus(true, ErrorType.ServerError)
                                    loadingFinished()
                                } else {
                                    val tkn = response.responseObject[0].toString()
                                    prefs.apiToken = tkn
                                    getPredictedTimes(token = tkn)
                                }
                            }

                            override fun onError(e: Throwable) {
                                loadingFinished()
                                handleError(e)
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
                                    handleError(ErrorType.ServerError)
                                    loadingFinished()
                                } else {
                                    val tramList = response.responseObject
                                    loadingFinished()
                                    handleError(null)
                                    handleData(stopId, tramList)
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
                                    handleError(e)
                                    loadingFinished()
                                }
                            }
                        })
        )
    }

    /**
     * Update the value of trams LiveData
     * @param stopId the calling stopId
     * @param tramList the list of Tram object that belongs to the stopId
     */
    private val viewList = mutableListOf<TramsListModel>()
    @Synchronized
    private fun handleData (stopId: String, tramList: List<Tram>) {
        if (viewList.size == _stops.size) viewList.clear()
        viewList.add(TramsListModel(stopId, tramList))
        if (viewList.size == _stops.size) {
            trams.value = viewList
            //handleAutoRefresh(viewList, Constants.COUNTDOWN_THRESHOLD)
        }
    }

    /**
     * If any of the trams is arriving in less than the threshold set in {@link Constants.COUNTDOWN_THRESHOLD}
     * then refresh data every.
     * @param viewList List of TramsListModel
     * @param refreshRate rate of refresh in milliseconds
     */
    private fun handleAutoRefresh(viewList: MutableList<TramsListModel>, refreshRate: Long) {
        if (viewList.any { it.belowCountdownThreshold() }) {
            viewModelScope.cancel()
            viewModelScope.launch {
                delay(refreshRate)
                refresh(_stops)
            }
        } else {
            viewModelScope.cancel()
        }
    }

    private fun loadingFinished() {
        loading.value = false
    }

    private fun isLoading() {
        loading.value = true
    }

    /**
     * Method to check type of error
     * Handling just network error or others for now
     */
    private fun handleError(e: Throwable) {
        if (e is IOException || e is SocketTimeoutException) {
            handleError(ErrorType.NetworkError)
        } else {
            handleError(ErrorType.ServerError)
        }
    }

    private fun handleError(type: ErrorType?){
        val errorStatus = if (type == null){ ErrorStatus(false)} else {
            ErrorStatus(true, type)
        }
        loadingError.value = errorStatus
    }

    /**
     * Dispose resources when there are no observers
     */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}