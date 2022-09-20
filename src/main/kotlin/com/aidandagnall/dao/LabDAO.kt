package com.aidandagnall.dao

import com.aidandagnall.models.Lab
import org.jetbrains.exposed.dao.id.EntityID

interface LabDAO {
    suspend fun getCurrentLabForRoom(roomId: EntityID<Int>): Lab?
    suspend fun getNextLabForRoom(roomId: EntityID<Int>): Lab?
    suspend fun getCurrentLabs(): List<Lab>
    suspend fun getNextLabs(): List<Lab>
}