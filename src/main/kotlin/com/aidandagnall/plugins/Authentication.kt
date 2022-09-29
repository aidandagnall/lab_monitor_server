package com.aidandagnall.plugins

import com.aidandagnall.Constants
import com.aidandagnall.Permissions
import com.aidandagnall.dao.UserDAOImpl
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

val dao = UserDAOImpl()

fun Application.configureAuthentication() {
    val jwk = JwkProviderBuilder(Constants.JWT_ISSUER)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt {
            verifier(jwk, Constants.JWT_ISSUER)
            validate { validateCredentials(it) }
        }
        Permissions.ALL.map { permission ->
            jwt(permission) {
                verifier(jwk, Constants.JWT_ISSUER)
                validate { validateCredentials(it, permission) }
            }
        }
    }
}

suspend fun validateCredentials(credential: JWTCredential, permission: String? = null): JWTPrincipal? {
    val containsAudience = credential.payload.audience.contains(Constants.JWT_AUDIENCE)
    val hasPermission = permission?.let { dao.checkUserPermission(credential.subject!!, it) } ?: true
    if (containsAudience && hasPermission) {
        return JWTPrincipal(credential.payload)
    }

    return null
}