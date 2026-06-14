package com.ngoctientnt.template.core.network.error

sealed class ApiException(
    message: String,
) : Exception(message) {

    class Http(
        val code: Int,
        message: String,
        val errorCode: String? = null,
        val rawBody: String? = null,
    ) : ApiException(message)

    class Network(
        message: String,
    ) : ApiException(message)

    class Unknown(
        message: String,
    ) : ApiException(message)
}
