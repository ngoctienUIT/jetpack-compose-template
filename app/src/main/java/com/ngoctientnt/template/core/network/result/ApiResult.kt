package com.ngoctientnt.template.core.network.result

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    data class HttpError(
        val code: Int,
        val message: String,
        val errorCode: String? = null,
        val rawBody: String? = null,
    ) : ApiResult<Nothing>

    data class NetworkError(val message: String) : ApiResult<Nothing>

    data class Unknown(val message: String) : ApiResult<Nothing>
}
