package com.aidandagnall.dao

import com.aidandagnall.models.Lab
import com.aidandagnall.models.Labs
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LabDAOImpl : LabDAO {
    override suspend fun getCurrentLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        Lab.find {
            Labs.startTime less DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.day eq LocalDateTime.now().dayOfWeek.value
        }.firstOrNull { it.rooms.any { room -> room.id == roomId } }
    }

    override suspend fun getNextLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        Lab.find {
            Labs.startTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.day eq LocalDateTime.now().dayOfWeek.value
        }.filter { it.rooms.any { room -> room.id == roomId }}.minByOrNull { it.startTime }
    }

    override suspend fun getCurrentLabs(): List<Lab> = transaction {
        Lab.find {
            Labs.startTime less DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.day eq LocalDateTime.now().dayOfWeek.value
        }.toList()
    }

    override suspend fun getNextLabs(): List<Lab> = transaction {
        Lab.find {
            Labs.startTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())
            Labs.day eq LocalDateTime.now().dayOfWeek.value
        }.toList()
    }
}