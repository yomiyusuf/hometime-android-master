package au.com.realestate.hometime.models

class ApiResponse<T> {
    var errorMessage: String? = null
    var hasError: Boolean? = null
    var hasResponse: Boolean? = null
    var responseObject: List<T>? = null
}


