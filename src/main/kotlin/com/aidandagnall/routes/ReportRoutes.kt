package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.ReportDAOImpl
import com.aidandagnall.models.ReportDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.reportRouting() {
    val dao = ReportDAOImpl()
    route("/report") {
        authenticate(Permissions.CREATE_REPORT) {

            post {
                val report = dao.createReport(call.receive())
                call.respond(HttpStatusCode.Created)
            }
        }

        authenticate(Permissions.READ_REPORTS) {
            get {
                call.respond(dao.allReports().map { ReportDTO(
                    it.id.value,
                    transaction { it.room.name },
                    it.popularity,
                    it.removalChance,
                    it.email,
                    it.time
                ) })
            }
        }

        authenticate(Permissions.DELETE_REPORT) {
            delete("/{id}") {
                val id = call.parameters["id"]?.toInt()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                dao.deleteReport(id)
                call.respond(HttpStatusCode.Accepted)
            }
        }
    }
}