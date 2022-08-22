package com.aidandagnall.models

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId

data class Room(
    val _id: Id<Room> = ObjectId().toId(),
    val name: String,
    val size: RoomSize,
    val type: RoomType,
    var popularity: Popularity?,
    var removalChance: RemovalChance?,
    var currentLab: Lab?,
    var nextLab: Lab?,
)

