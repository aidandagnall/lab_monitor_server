package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.Report
import com.aidandagnall.models.ReportDTO
import com.aidandagnall.models.ReportDTOWrapper
import com.aidandagnall.models.Room
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Route.reportRouting() {
    val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
    val roomCollection = db.getCollection<Room>()
    val reportCollection = db.getCollection<Report>()
    route("/report") {
        options {

        }
        post {
//            call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, "https://lab-monitor.herokuapp.com")
//            call.response.headers.append(HttpHeaders.AccessControlAllowHeaders, "x-requested-with")
//            call.response.headers.append(HttpHeaders.Vary, "Origin")
            val reportInfo: ReportDTO = call.receive()
            val room = roomCollection.findOne(Room::name eq reportInfo.room)
            if (room == null) {
                call.respond(HttpStatusCode.BadRequest)
            }

            if (reportInfo.popularity == null && reportInfo.removalChance == null) {
                call.respond(HttpStatusCode.BadRequest)
            }
            reportCollection.insertOne(Report(ZonedDateTime.now(ZoneId.of("Europe/London")).toInstant(), room!!._id, reportInfo.popularity, reportInfo.removalChance))

            call.respond(HttpStatusCode.Created)
        }
    }
}