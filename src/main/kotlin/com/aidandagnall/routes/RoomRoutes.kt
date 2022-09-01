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
    val moduleCollection = db.getCollection<com.aidandagnall.models.Module>()
    route("/room") {
        get {
            val recentReports = reportCollection.find(
                Report::time gte
                        Instant.now().minus(1, ChronoUnit.HOURS)
            ).toList()

            val now = ZonedDateTime.now(ZoneId.of("Europe/London"))
            val currentHourMinute = "${now.hour.toString().padStart(2, '0')}${now.minute.toString().padStart(2, '0')}"
            println(now.dayOfWeek.value)
            val todaysLabs = labCollection.find(
                Lab::day eq now.dayOfWeek.value
            ).toList()
            print(todaysLabs)

            val rooms = collection.find().toList().map { room ->
                room.apply {
                    val currentLabs = todaysLabs.filter { it.roomIds.contains(this._id) }.filter { it.startTime <= currentHourMinute && currentHourMinute < it.endTime }
                    val upcomingLabs = todaysLabs.filter { it.startTime > currentHourMinute }
                    if (currentLabs.isNotEmpty() || upcomingLabs.isNotEmpty()) {
                        removalChance = currentLabs.maxByOrNull { it.removalChance }?.removalChance
                        nextLab = todaysLabs.filter { it.roomIds.contains(this._id) }.filter { it.startTime > currentHourMinute }
                            .minByOrNull { it.startTime }?.apply {
                                module = moduleCollection.findOneById(moduleId)
                            }
                        currentLab = currentLabs.firstOrNull()?.apply {
                            module = moduleCollection.findOneById(moduleId)
                        };
                    }
                    val reports = recentReports.filter { it.room == this._id }
                    if (reports.isNotEmpty()) {
                        if (reports.any { it.removalChance != null })
                            removalChance = reports.groupingBy { it.removalChance }.eachCount().maxByOrNull { it.value }!!.key!!
                        if (reports.any { it.popularity != null })
                            popularity = reports.groupingBy { it.popularity }.eachCount().maxByOrNull { it.value }!!.key!!
                    }
                }
            }
            call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, "https://lab-monitor.herokuapp.com")
            call.response.headers.append(HttpHeaders.Vary, "Origin")
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