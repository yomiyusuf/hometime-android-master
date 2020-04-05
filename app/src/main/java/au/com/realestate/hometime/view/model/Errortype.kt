package au.com.realestate.hometime.view.model

sealed class ErrorType {
    object NetworkError : ErrorType()
    object ServerError : ErrorType()
}