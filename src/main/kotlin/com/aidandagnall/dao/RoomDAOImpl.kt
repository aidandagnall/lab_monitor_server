package com.aidandagnall.dao

import com.aidandagnall.models.Room
import org.jetbrains.exposed.sql.transactions.transaction

class RoomDAOImpl : RoomDAO {
    override suspend fun allRooms(): List<Room> = transaction {
       Room.all().toList()
    }
}