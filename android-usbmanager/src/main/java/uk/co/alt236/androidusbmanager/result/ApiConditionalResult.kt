package uk.co.alt236.androidusbmanager.result

sealed interface ApiConditionalResult<out T> {
    data class Success<T>(val result: T) : ApiConditionalResult<T>
    data class Error<T>(val error: Exception) : ApiConditionalResult<Nothing>
    data object ApiTooLow : ApiConditionalResult<Nothing>
}