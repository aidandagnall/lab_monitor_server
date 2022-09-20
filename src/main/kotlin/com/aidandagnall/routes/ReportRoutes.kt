package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.ReportDAOImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reportRouting() {
    val dao = ReportDAOImpl()
    route("/report") {
        authenticate(Permissions.CREATE_REPORT) {

            post {
                val report = dao.createReport(call.receive())
                call.respond(HttpStatusCode.Created, report)
            }
        }
    }
}