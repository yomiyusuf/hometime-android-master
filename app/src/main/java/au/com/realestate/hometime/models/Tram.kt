package au.com.realestate.hometime.models

import com.squareup.moshi.Json

class Tram {
    @Json(name = "Destination")
    var destination: String? = null
    @Json(name = "PredictedArrivalDateTime")
    var predictedArrival: String? = null
    @Json(name = "RouteNo")
    var routeNo: String? = null
}
