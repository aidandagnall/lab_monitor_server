package com.aidandagnall.plugins

import com.aidandagnall.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello world!")
        }
        roomRouting()
        labRouting()
        reportRouting()
        moduleRouting()
        issueRouting()
        userRouting()
    }
}
