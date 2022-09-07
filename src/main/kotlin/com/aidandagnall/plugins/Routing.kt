package com.aidandagnall.plugins

import com.aidandagnall.routes.labRouting
import com.aidandagnall.routes.moduleRouting
import com.aidandagnall.routes.reportRouting
import com.aidandagnall.routes.roomRouting
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
    }
    routing {
    }
}
