package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.Report
import com.aidandagnall.models.ReportDTO
import com.aidandagnall.models.Room
import com.aidandagnall.models.UserSession
import com.aidandagnall.tokenAuthenticationCredentials
import com.google.common.hash.Hashing
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Route.reportRouting() {
    val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
    val roomCollection = db.getCollection<Room>()
    val reportCollection = db.getCollection<Report>()
    val sessions = db.getCollection<UserSession>("user_session")
    route("/report") {
        authenticate {

            post {
                val tokenHash = call.request.authorization()
                    ?.let { token -> Hashing.sha256().hashString(token, StandardCharsets.UTF_8).toString() }
                val reportInfo: ReportDTO = call.receive()
                val room = roomCollection.findOne(Room::name eq reportInfo.room)
                if (room == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }

                if (reportInfo.popularity == null && reportInfo.removalChance == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }
                val session =
                    sessions.findOne { UserSession::tokenHash eq tokenHash } ?: return@post
                reportCollection.insertOne(Report(ZonedDateTime.now(ZoneId.of("Europe/London")).toInstant(), room!!._id, reportInfo.popularity, reportInfo.removalChance, email = session.email))

                call.respond(HttpStatusCode.Created)
            }
        }
    }
}