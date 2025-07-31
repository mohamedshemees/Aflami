package com.berlin.remote

import com.berlin.entity.AuthorizationException
import com.berlin.entity.BadRequestException
import com.berlin.entity.DataParseException
import com.berlin.entity.EmptyResponseException
import com.berlin.entity.ForbiddenException
import com.berlin.entity.NetworkException
import com.berlin.entity.NoInternetException
import com.berlin.entity.NotFoundException
import com.berlin.entity.RateLimitException
import com.berlin.entity.ServerException
import okio.IOException
import retrofit2.Response
import java.net.UnknownHostException


suspend fun <T> wrapApiResponse(
    request: suspend () -> Response<T>
): T {
    val response: Response<T>
    try {
        response = request()
    } catch (_: UnknownHostException) {
        throw NoInternetException(R.string.no_internet.toString())
    } catch (_: IOException) {
        throw NetworkException(R.string.network_error.toString())
    } catch (_: Exception) {
        throw DataParseException(R.string.unexpected_error.toString())
    }

    if (response.isSuccessful) {
        return response.body() ?: throw EmptyResponseException(R.string.empty_response.toString())
    } else {
        throw when (response.code()) {
            400 -> BadRequestException(response.message())
            401 -> AuthorizationException(R.string.unauthorized.toString())
            403 -> ForbiddenException(R.string.forbidden.toString())
            404 -> NotFoundException(R.string.not_found.toString())
            429 -> RateLimitException(R.string.rate_limit.toString())
            500 -> ServerException(R.string.internal_server_error.toString())
            else -> ServerException("${R.string.unexpected_error}: ${response.code()}")
        }
    }
}
