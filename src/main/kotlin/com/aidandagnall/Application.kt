package com.aidandagnall

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.aidandagnall.plugins.*
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*

//fun main() {
//    jsonMapper {
//        addModule(kotlinModule())
//    }
//    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
//        configureRouting()
//        configureSerialization()
//    }.start(wait = true)
//}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
}
