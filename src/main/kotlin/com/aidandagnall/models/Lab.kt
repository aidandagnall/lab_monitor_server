package com.aidandagnall.models

import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Lab(
    val _id: Id<Lab> = newId(),
    val moduleId: Id<Module>,
    var module: Module? = null,
    val day: Int,
    val startTime: String,
    val endTime: String,
    var removalChance: RemovalChance,
    val roomIds: List<Id<Room>>,
    var rooms: List<Room> = listOf(),
)
