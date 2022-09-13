package com.aidandagnall

import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.utils.io.charsets.*
import kotlin.text.Charsets

public fun AuthenticationConfig.token(
    name: String? = null,
    configure: TokenAuthenticationProvider.Config.() -> Unit
) {
    val provider = TokenAuthenticationProvider(TokenAuthenticationProvider.Config(name).apply(configure))
    register(provider)
}
class TokenAuthenticationProvider internal constructor(config: Config) : AuthenticationProvider(config) {
    internal val realm: String = config.realm

    internal val charset: Charset? = config.charset

    internal val authenticationFunction = config.authenticationFunction

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val call = context.call
        val token = call.request.authorization()?.removePrefix("Bearer ")
            ?.let { TokenCredential(it) }
        val principal = token?.let { authenticationFunction(call, it) }

        val cause = when {
            token == null -> AuthenticationFailedCause.NoCredentials
            principal == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }

        if (cause != null) {
            context.challenge("TokenAuth", cause) { challenge, call ->
                call.respond(UnauthorizedResponse(HttpAuthHeader.basicAuthChallenge(realm, charset)))
                challenge.complete()
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }

    public class Config internal constructor(name: String?) : AuthenticationProvider.Config(name) {
        internal var authenticationFunction: AuthenticationFunction<TokenCredential> = {
            throw NotImplementedError(
                "Token auth validate function is not specified. Use basic { validate { ... } } to fix."
            )
        }

        /**
         * Specifies a realm to be passed in the `WWW-Authenticate` header.
         */
        public var realm: String = "Ktor Server"

        /**
         * Specifies the charset to be used. It can be either `UTF_8` or `null`.
         * Setting `null` turns on a legacy mode (`ISO-8859-1`).
         */
        public var charset: Charset? = Charsets.UTF_8
            set(value) {
                if (value != null && value != Charsets.UTF_8) {
                    // https://tools.ietf.org/html/rfc7617#section-2.1
                    // 'The only allowed value is "UTF-8"; it is to be matched case-insensitively'
                    throw IllegalArgumentException("Basic Authentication charset can be either UTF-8 or null")
                }
                field = value
            }

        /**
         * Sets a validation function that checks a specified [UserPasswordCredential] instance and
         * returns [UserIdPrincipal] in a case of successful authentication or null if authentication fails.
         */
        public fun validate(body: suspend ApplicationCall.(TokenCredential) -> Principal?) {
            authenticationFunction = body
        }
    }

}

public data class TokenCredential(val token: String) : Credential
