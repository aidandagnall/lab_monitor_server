package com.aidandagnall.dao

import com.aidandagnall.models.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LabDAOImpl : LabDAO {

    override suspend fun getLabs(): List<Lab> = transaction {
        Lab.all().toList()
    }
    override suspend fun getCurrentLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        Lab.find {
            (Labs.startTime less DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.day eq LocalDateTime.now().dayOfWeek.value) and
            (Labs.room eq roomId)
        }.first()
    }

    override suspend fun getNextLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        Lab.find {
            (Labs.startTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.day eq LocalDateTime.now().dayOfWeek.value) and
            (Labs.room eq roomId)
        }.minByOrNull { it.startTime }
    }

    override suspend fun getCurrentLabs(): List<Lab> = transaction {
        Lab.find {
            (Labs.startTime less DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.endTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now())) and
            (Labs.day eq LocalDateTime.now().dayOfWeek.value)
        }.toList()
    }

    override suspend fun getNextLabs(): List<Lab> = transaction {
        Lab.find {
            Labs.startTime greater DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now()) and (Labs.day eq LocalDateTime.now().dayOfWeek.value)
        }.toList()
    }

    override suspend fun createLab(lab: CreateLabDTO): Lab = transaction {
        val _module = transaction { Module.findById(lab.module)}
        Lab.new {
            module = _module!!
            day = lab.day
            startTime = lab.startTime
            endTime = lab.endTime
            removalChance = lab.removalChance
            room = Room.find { Rooms.name eq lab.room }.first()
        }

    }

    override suspend fun deleteLab(id: Int): Unit = transaction {
        Lab.findById(id)?.delete()
    }
}