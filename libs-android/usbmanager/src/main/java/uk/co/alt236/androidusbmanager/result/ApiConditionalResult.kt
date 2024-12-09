package uk.co.alt236.androidusbmanager.result

sealed interface ApiConditionalResult<out T> {
    data class Success<T>(val result: T) : ApiConditionalResult<T>
    data class Error(val error: Exception) : ApiConditionalResult<Nothing>
    data object ApiTooLow : ApiConditionalResult<Nothing>

    fun getValueOrNull(): T? = when (this) {
        is Success -> this.result
        else -> null
    }
}