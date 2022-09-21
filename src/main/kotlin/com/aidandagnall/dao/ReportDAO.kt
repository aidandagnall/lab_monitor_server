package com.aidandagnall.dao

import com.aidandagnall.models.Report
import com.aidandagnall.models.ReportDTO
import org.jetbrains.exposed.dao.id.EntityID

interface ReportDAO {
    suspend fun createReport(dto: ReportDTO): Report?
    suspend fun allReports(): List<Report>
    suspend fun recentReportsForRoom(id: EntityID<Int>): List<Report>
    suspend fun recentReports(): List<Report>
}