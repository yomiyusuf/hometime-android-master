package au.com.realestate.hometime.util

/**
 * Class used to keep all constants in one location
 */
object Constants {

    //Endpoints
    const val BASE_URL = "http://ws3.tramtracker.com.au/TramTracker/RestService/"
    const val GET_TOKEN = "GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeiOS"
    const val GET_PREDICTED_TIME = "GetNextPredictedRoutesCollection/{stopId}/78/false/?aid=TTIOSJSON&cid=2"
}