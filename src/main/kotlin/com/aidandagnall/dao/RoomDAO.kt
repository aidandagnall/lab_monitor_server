package com.aidandagnall.dao

import com.aidandagnall.models.Room

interface RoomDAO {
    suspend fun allRooms(): List<Room>
}