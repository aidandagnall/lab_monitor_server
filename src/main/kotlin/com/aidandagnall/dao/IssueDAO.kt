package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO
import org.jetbrains.exposed.sql.transactions.transaction

interface IssueDAO {
    suspend fun getIssue(id: Int): Issue?

    suspend fun createIssue(dto: IssueDTO, _userId: String): Issue

    suspend fun completeIssue(id: Int, closedBy: String) : Issue?
    suspend fun deleteIssue(id: Int)
    suspend fun markIssueInProgress(id: Int, closedBy: String): Issue?
    suspend fun allIssues(): List<Issue>
}