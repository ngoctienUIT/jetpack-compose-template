package com.ngoctientnt.template.core.network.error

import com.ngoctientnt.template.core.network.dto.ApiErrorResponseDto
import com.ngoctientnt.template.core.network.result.ApiResult
import com.ngoctientnt.template.core.network.result.ApiUiErrors
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Singleton
class HttpErrorMapper @Inject constructor(
    private val json: Json,
) {

    fun map(httpException: HttpException): ApiResult.HttpError {
        val rawBody = httpException.response()?.errorBody()?.string()
        val parsedError = rawBody?.let { body ->
            runCatching { json.decodeFromString<ApiErrorResponseDto>(body) }.getOrNull()
        }

        return ApiResult.HttpError(
            code = httpException.code(),
            message = parsedError?.message ?: httpException.message() ?: ApiUiErrors.UNKNOWN,
            errorCode = parsedError?.code,
            rawBody = rawBody,
        )
    }
}
