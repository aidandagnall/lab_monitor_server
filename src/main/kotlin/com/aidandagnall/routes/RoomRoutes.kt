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
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.roomRouting() {
    val roomDAO = RoomDAOImpl()
    val labDAO = LabDAOImpl()
    val reportDAO = ReportDAOImpl()
    route("/room") {
        authenticate(Permissions.READ_ROOMS) {
            get {
                val reports = reportDAO.recentReports()
                val currentLabs = labDAO.getCurrentLabs()
                val nextLabs = labDAO.getNextLabs()
                // TODO: STOP THIS FROM MAKING SO MANY CALLS
                call.respond(roomDAO.allRooms().map {
                    RoomDTO(
                        it,
                        currentLabs.firstOrNull { lab ->
                            lab.rooms.map { lab.id }.contains(it.id)
                        },
                        nextLabs.filter { lab -> lab.rooms.map { lab.id }.contains(it.id) }
                            .minByOrNull { lab -> lab.startTime },
                        transaction { reports.filter { report -> report.room.id == it.id } } )
                })

            }
            get("{name?}") {

            }
        }
    }
}