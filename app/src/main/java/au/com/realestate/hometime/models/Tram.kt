package au.com.realestate.hometime.models

import com.google.gson.annotations.SerializedName

data class Tram (
    @SerializedName("Destination")
    var destination: String,

    @SerializedName("PredictedArrivalDateTime")
    var predictedArrival: String,

    @SerializedName("RouteNo")
    var routeNo: String? = null
)
