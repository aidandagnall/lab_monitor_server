package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO
import com.aidandagnall.models.IssueStatus
import com.aidandagnall.models.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class IssueDAOImpl : IssueDAO {

    override suspend fun getIssue(id: Int): Issue? = transaction { Issue.findById(id) }
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

    override suspend fun completeIssue(id: Int, closedBy: String) : Issue? = transaction {
        Issue.findById(id)?.let {
            it.status = IssueStatus.RESOLVED
            it.closedBy = User.findById(closedBy)!!
            return@transaction it
        }
        null
    }

    override suspend fun markIssueAsNew(id: Int, closedBy: String) : Issue? = transaction {
        Issue.findById(id)?.let {
            it.status = IssueStatus.NEW
            it.closedBy = User.findById(closedBy)!!
            return@transaction it
        }
        null
    }


    override suspend fun deleteIssue(id: Int): Unit = transaction {
        Issue.findById(id)?.delete()
    }

    override suspend fun markIssueInProgress(id: Int, closedBy: String): Issue? = transaction {
        Issue.findById(id)?.let {
            it.status = IssueStatus.IN_PROGRESS
            it.closedBy = User.findById(closedBy)!!
            return@transaction it
        }
        null
    }
    override suspend fun allIssues(): List<Issue> = transaction { Issue.all().sortedBy { it.dateSubmitted }.toList() }

}