package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

data class LabDTO(
    val id: Int?,
    val module: ModuleDTO,
    val day: Int,
    val startTime: String,
    val endTime: String,
    val removalChance: RemovalChance,
    val rooms: List<String>,
) {
    companion object {
        fun fromLab(lab: Lab): LabDTO {
            return LabDTO(
                id = lab.id.value,
                module = transaction { ModuleDTO.fromModule(lab.module) },
                day = lab.day,
                startTime = lab.startTime,
                endTime = lab.endTime,
                removalChance = lab.removalChance,
                rooms = transaction { listOf(lab.room.name) }
            )
        }
    }
}

data class CreateLabDTO(
    val module: Int,
    val day: Int,
    val startTime: String,
    val endTime: String,
    val removalChance: RemovalChance,
    val room: String,
)

class Lab(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Lab>(Labs)
    var module by Module referencedOn Labs.moduleId
    var day by Labs.day
    var startTime by Labs.startTime
    var endTime by Labs.endTime
    var removalChance by Labs.removalChance
    var room by Room referencedOn Labs.room
}


object Labs : IntIdTable() {
    val moduleId = reference("module_id", Modules.id)
    val day = integer("day")
    val startTime = varchar("start_time", 4)
    val endTime = varchar("end_time", 4)
    val removalChance = enumeration("removal_chance", RemovalChance::class)
    val room = reference("room_id", Rooms)
}

//object LabRooms : Table() {
//    val labId = reference("lab_id", Labs)
//    val roomId = reference("room_id", Rooms)
//    override val primaryKey = PrimaryKey(roomId, labId)
//}