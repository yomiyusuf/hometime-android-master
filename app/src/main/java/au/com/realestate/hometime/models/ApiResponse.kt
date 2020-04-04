package au.com.realestate.hometime.models

data class ApiResponse<T> (
    var errorMessage: String?,
    var hasError: Boolean,
    var hasResponse: Boolean,
    var responseObject: List<T>
)