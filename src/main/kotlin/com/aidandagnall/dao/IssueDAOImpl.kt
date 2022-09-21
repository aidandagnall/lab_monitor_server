package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO
import com.aidandagnall.models.IssueStatus
import com.aidandagnall.models.User
import org.jetbrains.exposed.sql.transactions.transaction

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
        }
    }

    override suspend fun allIssues(): List<Issue> = Issue.all().toList()

}