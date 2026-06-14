package com.ngoctientnt.template.core.network.result

import com.ngoctientnt.template.core.network.error.ApiException

fun <T> ApiResult<T>.getDataOrNull(): T? {
    return (this as? ApiResult.Success)?.data
}

fun ApiResult<*>.displayMessage(): String {
    return when (this) {
        is ApiResult.HttpError -> message
        is ApiResult.NetworkError -> message
        is ApiResult.Unknown -> message
        is ApiResult.Success -> ""
    }
}

fun ApiResult<*>.uiErrorMessageOrNull(): String? {
    return when (this) {
        is ApiResult.Success -> null
        is ApiResult.HttpError -> message
        is ApiResult.NetworkError -> ApiUiErrors.NETWORK
        is ApiResult.Unknown -> ApiUiErrors.UNKNOWN
    }
}

inline fun <T> ApiResult<T>.handleResult(
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit,
) {
    when (this) {
        is ApiResult.Success -> onSuccess(data)
        else -> onError(uiErrorMessageOrNull().orEmpty())
    }
}

suspend fun <T> ApiResult<T>.handleResultSuspend(
    onSuccess: suspend (T) -> Unit,
    onError: suspend (String) -> Unit,
) {
    when (this) {
        is ApiResult.Success -> onSuccess(data)
        else -> onError(uiErrorMessageOrNull().orEmpty())
    }
}

fun ApiResult<*>.isSuccess(): Boolean = this is ApiResult.Success

fun ApiResult<*>.isError(): Boolean = !isSuccess()

suspend fun <T, R> ApiResult<T>.flatMap(
    transform: suspend (T) -> ApiResult<R>,
): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> transform(data)
        is ApiResult.HttpError -> this
        is ApiResult.NetworkError -> this
        is ApiResult.Unknown -> this
    }
}

suspend fun <T, R> ApiResult<T>.mapSuccess(
    transform: suspend (T) -> R,
): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> {
            try {
                ApiResult.Success(transform(data))
            } catch (error: kotlinx.coroutines.CancellationException) {
                throw error
            } catch (error: Exception) {
                ApiResult.Unknown(error.message ?: "Transform failed")
            }
        }
        is ApiResult.HttpError -> this
        is ApiResult.NetworkError -> this
        is ApiResult.Unknown -> this
    }
}

fun ApiResult<*>.toApiException(): ApiException {
    return when (this) {
        is ApiResult.HttpError -> ApiException.Http(
            code = code,
            message = message,
            errorCode = errorCode,
            rawBody = rawBody,
        )
        is ApiResult.NetworkError -> ApiException.Network(message)
        is ApiResult.Unknown -> ApiException.Unknown(message)
        is ApiResult.Success -> ApiException.Unknown("Cannot convert success result to ApiException")
    }
}
