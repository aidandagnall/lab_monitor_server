package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.LocalDateTime

class Issue(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Issue>(Issues)
    var location by Issues.location
    var email by Issues.email
    var category by Issues.category
    var subCategory by Issues.subCategory
    var subSubCategory by Issues.subSubCategory
    var description by Issues.description
    var status by Issues.status
    var dateSubmitted by Issues.dateSubmitted
}

data class IssueDTO(
    val id: Int?,
    val location: String,
    val email: String,
    val category: String,
    val subCategory: String?,
    val subSubCategory: String?,
    val description: String?,
    val status: String?,
    val dateSubmitted: LocalDateTime?
) {
    companion object {
        fun fromIssue(issue: Issue): IssueDTO = IssueDTO(
            issue.id.value,
            issue.location,
            issue.email,
            issue.category,
            issue.subCategory,
            issue.subSubCategory,
            issue.description,
            issue.status.name,
            issue.dateSubmitted,
        )

    }
}

enum class IssueStatus {
    NEW,
    IN_PROGRESS,
    RESOLVED,
}

object Issues : IntIdTable() {
    val location = varchar("location", 7)
    val email = varchar("email", 40)
    val category = varchar("category", 30)
    val subCategory = varchar("sub_category", 30).nullable()
    val subSubCategory = varchar("sub_sub_category", 30).nullable()
    val description = varchar("description", 500).nullable()
    val status = enumeration("status", IssueStatus::class)
    val dateSubmitted = datetime("date_submitted")
}
