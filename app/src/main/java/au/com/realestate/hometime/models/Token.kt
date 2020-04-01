package au.com.realestate.hometime.models

import com.google.gson.annotations.SerializedName

data class ApiToken(
        val errorMessage: String?,
        val hasError: Boolean,
        val hasResponse: Boolean,
        val responseObject: List<TokenResponseObject>
){
    var token = if (hasResponse) responseObject[0].deviceToken else null
}

data class TokenResponseObject (
        @SerializedName("DeviceToken")
        val deviceToken: String
)