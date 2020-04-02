package au.com.realestate.hometime.models

import com.google.gson.annotations.SerializedName

data class ApiToken (
        @SerializedName("DeviceToken")
        val deviceToken: String
)