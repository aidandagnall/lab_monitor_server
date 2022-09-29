package com.aidandagnall.dao

import com.aidandagnall.models.CreateLabDTO
import com.aidandagnall.models.Lab
import com.aidandagnall.models.LabDTO
import org.jetbrains.exposed.dao.id.EntityID

interface LabDAO {
    suspend fun getLabs(): List<Lab>
    suspend fun getCurrentLabForRoom(roomId: EntityID<Int>): Lab?
    suspend fun getNextLabForRoom(roomId: EntityID<Int>): Lab?
    suspend fun getCurrentLabs(): List<Lab>
    suspend fun getNextLabs(): List<Lab>
    suspend fun createLab(lab: CreateLabDTO): Lab
    suspend fun deleteLab(id: Int)
}