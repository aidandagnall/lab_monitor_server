package com.aidandagnall.models

import com.aidandagnall.models.LabRooms.references
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class LabDTO(
    val moduleCode: String,
    val day: Int,
    val startTime: String,
    val endTime: String,
    val removalChance: RemovalChance,
    val rooms: List<String>,
)

class Lab(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Lab>(Labs)
    var moduleId by Module referencedOn Modules.id
    var day by Labs.day
    var startTime by Labs.startTime
    var endTime by Labs.endTime
    var removalChance by Labs.removalChance
    var rooms by Room via LabRooms
}


object Labs : IntIdTable() {
    val moduleId = reference("module_id", Modules.id)
    val day = integer("day")
    val startTime = varchar("start_time", 4)
    val endTime = varchar("end_time", 4)
    val removalChance = enumeration("removal_chance", RemovalChance::class)
}

object LabRooms : Table() {
    val labId = reference("lab_id", Labs)
    val roomId = reference("room_id", Rooms.id)
    override val primaryKey = PrimaryKey(roomId, labId)
}