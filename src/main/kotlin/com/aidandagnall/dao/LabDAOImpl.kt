package com.aidandagnall.dao

import com.aidandagnall.models.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LabDAOImpl : LabDAO {

    val zone = ZoneId.of("Europe/London")
    private fun getCurrentTime(): String = DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now(zone))
    override suspend fun getLabs(): List<Lab> = transaction {
        Lab.all().toList()
    }
    override suspend fun getCurrentLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        val currentTime = getCurrentTime()
        Lab.find {
            (Labs.startTime less currentTime) and
            (Labs.endTime greater currentTime) and
            (Labs.day eq LocalDateTime.now(zone).dayOfWeek.value) and
            (Labs.room eq roomId)
        }.first()
    }

    override suspend fun getNextLabForRoom(roomId: EntityID<Int>): Lab? = transaction {
        val currentTime = getCurrentTime()
        Lab.find {
            (Labs.startTime greater currentTime) and
            (Labs.endTime greater currentTime) and
            (Labs.day eq LocalDateTime.now(zone).dayOfWeek.value) and
            (Labs.room eq roomId)
        }.minByOrNull { it.startTime }
    }

    override suspend fun getCurrentLabs(): List<Lab> = transaction {
        val currentTime = getCurrentTime()
        Lab.find {
            (Labs.startTime less currentTime) and
            (Labs.endTime greater currentTime) and
            (Labs.day eq LocalDateTime.now(zone).dayOfWeek.value)
        }.toList()
    }

    override suspend fun getNextLabs(): List<Lab> = transaction {
        val currentTime = getCurrentTime()
        Lab.find {
            Labs.startTime greater currentTime and (Labs.day eq LocalDateTime.now(zone).dayOfWeek.value)
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