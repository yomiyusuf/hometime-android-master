package au.com.realestate.hometime.models

import com.google.gson.annotations.SerializedName

data class Token (
        @SerializedName("DeviceToken")
        val deviceToken: String
)