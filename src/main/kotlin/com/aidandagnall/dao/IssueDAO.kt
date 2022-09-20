package com.aidandagnall.dao

import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO

interface IssueDAO {
    suspend fun createIssue(dto: IssueDTO, _email: String): Issue
    suspend fun allIssues(): List<Issue>
}