package com.aidandagnall

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.aidandagnall.plugins.*
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.cdimascio.dotenv.dotenv
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
        anyHost()
    }
    configureRouting()
    configureSerialization()
}
