package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.LabDAOImpl
import com.aidandagnall.models.CreateLabDTO
import com.aidandagnall.models.LabDTO
import com.aidandagnall.models.ModuleDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.labRouting() {
    val dao = LabDAOImpl()

    route("/labs") {
        authenticate(Permissions.READ_ROOMS) {
            get {
                val labs = suspendedTransactionAsync{ dao.getLabs().map { LabDTO(
                    it.id.value,
                    ModuleDTO(it.module.id.value, it.module.code, it.module.abbreviation, it.module.name, listOf(it.module.convenor)),
                    it.day,
                    it.startTime,
                    it.endTime,
                    it.removalChance,
                    listOf(it.room.name)
                )} }
                call.respond(labs.await())
            }
        }
        authenticate(Permissions.CREATE_LAB) {
            post {
                val dto = call.receive<CreateLabDTO>()
                dao.createLab(dto)
                call.respond(HttpStatusCode.Created)
            }
        }
        authenticate(Permissions.DELETE_LAB) {
            delete("/{id}") {
                dao.deleteLab(Integer.parseInt(call.parameters["id"]))
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}