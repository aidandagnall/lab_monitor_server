package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO

interface IssueDAO {
    suspend fun createIssue(dto: IssueDTO, _userId: String): Issue

    suspend fun completeIssue(id: Int) : Boolean
    suspend fun deleteIssue(id: Int)
    suspend fun allIssues(): List<Issue>
}