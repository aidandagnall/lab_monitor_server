package com.aidandagnall.dao

import com.aidandagnall.models.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class ReportDAOImpl : ReportDAO {
    override suspend fun createReport(dto: ReportDTO): Report? {
        val _room = transaction {
            Room.find {
                Rooms.name eq dto.room.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }.first()
        }

        return transaction {
            Report.new {
                time = LocalDateTime.now()
                room = _room
                popularity = dto.popularity
                removalChance = dto.removalChance
                email = dto.email
            }
        }
    }

    override suspend fun allReports(): List<Report> = transaction { Report.all().sortedByDescending { it.time }.toList() }

    override suspend fun recentReportsForRoom(id: EntityID<Int>): List<Report> = transaction {
        Report.find {
            Reports.room eq id
            Reports.time greater LocalDateTime.now().minusHours(1)
        }.toList()
    }

    override suspend fun recentReports(): List<Report> = transaction {
        Report.find {
            Reports.time greater LocalDateTime.now().minusHours(1)
        }.with(Report::room).toList()
    }

    override suspend fun deleteReport(id: Int) = transaction {
        Report.findById(id)?.delete()
    }
}