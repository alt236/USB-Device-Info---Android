package uk.co.alt236.usbinfo.database.model

sealed interface DbResult<out T> {
    data class Success<T>(val result: T) : DbResult<T>
    data object DbFailedToOpen : DbResult<Nothing>
    data object DbNotPresent : DbResult<Nothing>
    data class ErrorGeneric(val error: Exception) : DbResult<Nothing>

    fun getValueOrNull(): T? = when (this) {
        is Success -> this.result
        else -> null
    }
}