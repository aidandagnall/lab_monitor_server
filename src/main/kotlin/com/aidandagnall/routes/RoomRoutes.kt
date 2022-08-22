package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.*
import com.fasterxml.jackson.databind.Module
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import java.util.*

fun Route.roomRouting() {
    val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
    val collection = db.getCollection<Room>()
    val reportCollection = db.getCollection<Report>()
    val labCollection = db.getCollection<Lab>()
    route("/room") {
        get {
            println(ZonedDateTime.now(ZoneId.of("Europe/London")).minusHours(1).toInstant().toString())
            println(Instant.now().minus(1, ChronoUnit.HOURS).toString())
//            var recentReports = reportCollection.find("{\"\$date\" : { \$gte: new Date(ISODate().getTime() - 1000 * 60 * 60)}}")
//            val recentReports = reportCollection.find(
//                Report::time gte Date.from(Instant.now().minus(1, ChronoUnit.HOURS))
//            ).toList()
            val recentReports = reportCollection.find(
                Report::time gte
                        Instant.now().minus(1, ChronoUnit.HOURS)
//                        ZonedDateTime.now(ZoneId.of("Europe/London")).minusHours(1).toInstant()
            ).toList()
            println(recentReports)
            val now = ZonedDateTime.now(ZoneId.of("Europe/London"))
            val currentHourMinute = "${now.hour.toString().padStart(2, '0')}${now.minute.toString().padStart(2, '0')}"
            val currentLabs = labCollection.find(
                Lab::startTime lte currentHourMinute,
                Lab::endTime gt currentHourMinute,
                Lab::day eq now.dayOfWeek.value
            ).toList()
            val rooms = collection.find().toList().map { room ->
                room.apply {
                    val labs = currentLabs.filter { it.roomIds.contains(this._id) }
                    if (labs.isNotEmpty()) {
                        removalChance = labs.maxByOrNull { it.removalChance }?.removalChance
                    }
                    val reports = recentReports.filter { it.room == this._id }
                    println(reports)
                    if (reports.isNotEmpty()) {
                        if (reports.any { it.removalChance != null })
                            removalChance = reports.groupingBy { it.removalChance }.eachCount().maxByOrNull { it.value }!!.key!!
                        if (reports.any { it.popularity != null })
                            popularity = reports.groupingBy { it.popularity }.eachCount().maxByOrNull { it.value }!!.key!!
                    }
//                    if (this.name == "B52") {
//                        this.currentLab = Lab(newId(), newId(), com.aidandagnall.models.Module(newId(), "COMP1003", "Introduction to Software Engineering", listOf("Dr Max L Wilson")), day = 3, startTime = "1100", endTime = "1400", removalChance = RemovalChance.low, roomIds = listOf(), rooms = listOf(Room(_id = newId(), name = "B52", size = RoomSize.medium, type = RoomType.lab, currentLab = null, nextLab = null, popularity = Popularity.medium, removalChance = RemovalChance.low )))
//                    }
                }
            }
            call.respond(rooms)
        }
        get("{name?}") {
            val room = collection.findOne(Room::name eq call.parameters["name"]?.uppercase())
            if (room == null)
                call.respondText(status = HttpStatusCode.NotFound, text = "Room not found.")
            call.respond(room!!)

        }
    }
}