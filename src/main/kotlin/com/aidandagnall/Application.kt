package com.aidandagnall

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.aidandagnall.plugins.*
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.cdimascio.dotenv.dotenv

fun main() {
    jsonMapper {
        addModule(kotlinModule())
    }
    embeddedServer(Netty, port = 8080) {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
