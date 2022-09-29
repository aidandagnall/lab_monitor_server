package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO
import com.aidandagnall.models.IssueStatus
import com.aidandagnall.models.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class IssueDAOImpl : IssueDAO {
    override suspend fun createIssue(dto: IssueDTO, _userId: String) : Issue = transaction {
        Issue.new {
            email = User.findById(_userId)!!.email
            location = dto.location
            category = dto.category
            subCategory = dto.subCategory
            subSubCategory = dto.subSubCategory
            description = dto.description
            status = IssueStatus.NEW
            dateSubmitted = LocalDateTime.now()
        }
    }

    override suspend fun completeIssue(id: Int) : Boolean = transaction {
        val issue = Issue.findById(id)
        issue?.let {
            it.status = IssueStatus.RESOLVED
            return@transaction true
        }
        return@transaction false
    }

    override suspend fun deleteIssue(id: Int): Unit = transaction {
        Issue.findById(id)?.delete()
    }
    override suspend fun allIssues(): List<Issue> = transaction { Issue.all().sortedBy { it.dateSubmitted }.toList() }

}