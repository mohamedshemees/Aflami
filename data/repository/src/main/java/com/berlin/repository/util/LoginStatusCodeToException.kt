package com.berlin.repository.util

import com.berlin.entity.InvalidLoginApiKeyException
import com.berlin.entity.InvalidLoginTokenException
import com.berlin.entity.InvalidUsernameOrPasswordException
import com.berlin.entity.AuthorizationException
import com.berlin.repository.R

fun String?.toException() = when (this) {
    String.Companion.invalidToken -> InvalidLoginTokenException(R.string.invalid_token.toString())
    String.Companion.invalidUsernameOrPassword -> InvalidUsernameOrPasswordException(R.string.invalid_username_password.toString())
    String.Companion.invalidApiKey -> InvalidLoginApiKeyException(R.string.invalid_api_key.toString())
    String.Companion.sessionDenied -> AuthorizationException(R.string.session_denied.toString())
    else -> Exception("${R.string.unknown_error} $this")
}
