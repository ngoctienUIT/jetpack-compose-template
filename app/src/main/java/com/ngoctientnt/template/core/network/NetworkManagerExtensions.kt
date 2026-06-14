package com.ngoctientnt.template.core.network

import com.ngoctientnt.template.core.network.result.ApiResult
import com.ngoctientnt.template.core.network.result.flatMap

suspend fun <A, B> NetworkManager.safeApiChain(
    first: suspend () -> A,
    second: suspend (A) -> B,
): ApiResult<B> {
    return safeApiCall(first).flatMap { dataA ->
        safeApiCall { second(dataA) }
    }
}

suspend fun <A, B, C> NetworkManager.safeApiChain(
    first: suspend () -> A,
    second: suspend (A) -> B,
    third: suspend (B) -> C,
): ApiResult<C> {
    return safeApiCall(first)
        .flatMap { dataA -> safeApiCall { second(dataA) } }
        .flatMap { dataB -> safeApiCall { third(dataB) } }
}
