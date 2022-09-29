package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class RoomDTO(
    val name: String,
    val size: RoomSize,
    val type: RoomType,
    var popularity: Popularity?,
    var removalChance: RemovalChance?,
    var currentLab: LabDTO?,
    var nextLab: LabDTO?,
) {
    constructor(room: Room, currentLab: LabDTO?, nextLab: LabDTO?, reports: List<ReportDTO>) : this(
        name = room.name,
        size = room.size,
        type = room.type,
        popularity = reports.mapNotNull { it.popularity }.maxByOrNull { it.ordinal },
        removalChance = reports.mapNotNull { it.removalChance }.maxByOrNull { it.ordinal } ?: currentLab?.removalChance,
        currentLab = currentLab,
        nextLab = nextLab
    )
}

class Room(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<Room>(Rooms)
    var name by Rooms.name
    var size by Rooms.size
    var type by Rooms.type
}

object Rooms : IntIdTable() {
    val name = varchar("name", 15)
    val size = enumeration("size", RoomSize::class)
    val type = enumeration("type", RoomType::class)
}

