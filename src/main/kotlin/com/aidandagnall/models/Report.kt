package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

//data class Report(val time: LocalDateTime, val room: Int, val popularity: Popularity?, val removalChance: RemovalChance?, val email: String?)

data class ReportDTO(val id: Int?, val room: String, val popularity: Popularity?, val removalChance: RemovalChance?, val email: String, val time: LocalDateTime?)

class Report(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Report>(Reports)
    var time by Reports.time
    var room by Room referencedOn Reports.room
    var popularity by Reports.popularity
    var removalChance by Reports.removalChance
    var email by Reports.email
}

object Reports : IntIdTable() {
    val time = datetime("time")
    val room = reference("room_id", Rooms.id)
    val popularity = enumeration("popularity", Popularity::class).nullable()
    val removalChance = enumeration("removal_chance", RemovalChance::class).nullable()
    val email = varchar("email", 40)
}