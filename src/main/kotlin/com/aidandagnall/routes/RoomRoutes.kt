package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.LabDAOImpl
import com.aidandagnall.dao.ReportDAOImpl
import com.aidandagnall.dao.RoomDAOImpl
import com.aidandagnall.models.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.roomRouting() {
    val roomDAO = RoomDAOImpl()
    val labDAO = LabDAOImpl()
    val reportDAO = ReportDAOImpl()
    route("/room") {
        authenticate(Permissions.READ_ROOMS) {
            get {
                val reports = newSuspendedTransaction { reportDAO.recentReports().map { ReportDTO( it.id.value, it.room.name, it.popularity, it.removalChance, "", it.time) } }
                val currentLabs = labDAO.getCurrentLabs().map { LabDTO.fromLab(it) }
                val nextLabs = labDAO.getNextLabs().map { LabDTO.fromLab(it) }
                call.respond(roomDAO.allRooms().map {
                    RoomDTO(
                        it,
                        currentLabs.firstOrNull { lab ->
                            it.name in lab.rooms
                        },
                        nextLabs.filter { lab -> it.name in lab.rooms }
                            .minByOrNull { lab -> lab.startTime },

                        reports.filter { report -> it.name == report.room},)
                })

            }
            get("{name?}") {

            }
        }
    }
}