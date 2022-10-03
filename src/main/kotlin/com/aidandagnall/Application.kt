package com.aidandagnall

import com.aidandagnall.dao.DatabaseFactory
import com.aidandagnall.plugins.*
//import com.auth0.jwk.JwkProvider
//import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        allowHost("lab-monitor.herokuapp.com", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader("authorization")
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
    DatabaseFactory.init()
    configureRouting()
    configureSerialization()
    configureAuthentication()
}
