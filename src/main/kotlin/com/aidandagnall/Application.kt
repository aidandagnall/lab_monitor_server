package com.aidandagnall

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.aidandagnall.plugins.*
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.cdimascio.dotenv.dotenv

fun main() {
    val mapper = jsonMapper {
        addModule(kotlinModule())
    }
    embeddedServer(Netty, port = dotenv()["PORT"].toInt()) {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
