package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.conversions.Bson
import org.litote.kmongo.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun Route.labRouting() {

    route("/labs") {
        get("/now") {
            val now = ZonedDateTime.now(ZoneId.of("Europe/London"))
            val currentHourMinute = "${now.hour.toString().padStart(2, '0')}${now.minute.toString().padStart(2, '0')}"
            val currentLabs = getLabs( true,
                Lab::startTime lte currentHourMinute,
                Lab::endTime gt currentHourMinute,
                Lab::day eq now.dayOfWeek.value
            )

            call.respond(currentLabs)
        }
        get("/today") {
            val now = ZonedDateTime.now(ZoneId.of("Europe/London"))
            val currentLabs = getLabs( false,
                Lab::day eq now.dayOfWeek.value
            )

            call.respond(currentLabs)
        }
        get {
            val labs = getLabs(false)
            call.respond(labs)
        }
        get("/room/{name?}") {
            val labs = getLabs(false).filter { lab -> lab.rooms.any { it.name == call.parameters["name"]?.uppercase() } }

            call.respond(labs)
        }
    }
}

fun getLabs(getReportInfo: Boolean, vararg filters: Bson?): List<Lab> {

    val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
    val labCollection = db.getCollection<Lab>()
    val roomCollection = db.getCollection<Room>()
    val moduleCollection = db.getCollection<Module>()
    val reportCollection = db.getCollection<Report>()
    return labCollection.find(and(*filters))
        .toList()
        .map{lab ->
            lab.apply {
                module = moduleCollection.findOneById(lab.moduleId)
                rooms = roomIds.map {
                    roomCollection.findOneById(it)!!
                }
                if (!getReportInfo) return@apply
                val recentReports = reportCollection.find(
                        Report::time lte Date.from(ZonedDateTime.now(ZoneId.of("Europe/London")).minusHours(1).toInstant())
                    ).toList()
                    .filter { roomIds.contains(it.room) }
                if (recentReports.isNotEmpty()) {
                    if (recentReports.any { it.removalChance != null })
                        removalChance = recentReports.map { it }.groupingBy { it.removalChance }.eachCount().maxByOrNull { it.value }!!.key!!
                    if (recentReports.any { it.popularity != null })
                        rooms.map { room ->
                            room.apply { popularity = recentReports.map { it }.groupingBy { it.popularity }.eachCount().maxByOrNull { it.value }!!.key!! }
                        }
                }
            }
        }
}