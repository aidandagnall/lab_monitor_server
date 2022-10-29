package com.aidandagnall

import com.aidandagnall.dao.DatabaseFactory
import com.aidandagnall.plugins.*
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    if (dotenv()["ALLOW_WEB"] == "true") {
        install(CORS) {
            allowHost(dotenv()["JWT_AUDIENCE"], schemes = listOf("http", "https"))
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
    }
    DatabaseFactory.init()
    configureRouting()
    configureSerialization()
    configureAuthentication()
}
