package com.aidandagnall.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Issue(
    val _id: Id<Issue> = newId(),
    val location: String,
    val email: String,
    val category: String,
    val subCategory: String?,
    val subSubCategory: String?,
    val description: String?,
    val status: IssueStatus = IssueStatus.NEW,
)

enum class IssueStatus {
    NEW,
    IN_PROGRESS,
    RESOLVED,
}
