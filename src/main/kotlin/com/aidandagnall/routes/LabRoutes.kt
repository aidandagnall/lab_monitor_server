package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.LabDAOImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.labRouting() {
    val dao = LabDAOImpl()

    route("/labs") {
        authenticate(Permissions.CREATE_LAB) {
            post {
                dao.createLab(call.receive())
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}