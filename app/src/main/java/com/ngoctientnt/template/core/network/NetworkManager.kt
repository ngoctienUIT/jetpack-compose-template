package com.ngoctientnt.template.core.network

import com.ngoctientnt.template.core.network.error.HttpErrorMapper
import com.ngoctientnt.template.core.network.paging.PagedResponse
import com.ngoctientnt.template.core.network.paging.PagingApiResult
import com.ngoctientnt.template.core.network.result.ApiResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

@Singleton
class NetworkManager @Inject constructor(
    private val httpErrorMapper: HttpErrorMapper,
) {

    suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(block())
        } catch (error: CancellationException) {
            throw error
        } catch (error: HttpException) {
            httpErrorMapper.map(error)
        } catch (error: IOException) {
            ApiResult.NetworkError(error.message ?: DEFAULT_NETWORK_ERROR)
        } catch (error: Exception) {
            ApiResult.Unknown(error.message ?: DEFAULT_UNKNOWN_ERROR)
        }
    }

    suspend fun <T> safePagingApiCall(
        block: suspend () -> PagedResponse<T>,
    ): PagingApiResult<T> = safeApiCall(block)

    companion object {
        private const val DEFAULT_NETWORK_ERROR = "Network error"
        private const val DEFAULT_UNKNOWN_ERROR = "Unknown error"
    }
}
