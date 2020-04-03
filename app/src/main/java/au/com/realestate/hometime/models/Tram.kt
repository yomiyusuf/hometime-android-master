package au.com.realestate.hometime.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Tram (
    @SerializedName("Destination")
    var destination: String,

    @SerializedName("PredictedArrivalDateTime")
    var predictedArrival: String,

    @SerializedName("RouteNo")
    var routeNo: String? = null
) {
    /**
     * Method to convert the predictedArrival string to a standard date format
     * @return Date
     */
    fun readablePredictedArrival(): Date {
        val startIndex = predictedArrival.indexOf("(") + 1
        val endIndex = predictedArrival.indexOf("+")
        val date = predictedArrival.substring(startIndex, endIndex)

        val unixTime = java.lang.Long.parseLong(date)
        return Date(unixTime)
    }
}
