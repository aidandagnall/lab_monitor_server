package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Issue(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Issue>(Issues)
    var location by Issues.location
    var email by Issues.email
    var category by Issues.category
    var subCategory by Issues.subCategory
    var subSubCategory by Issues.subSubCategory
    var description by Issues.description
    var status by Issues.status
}

data class IssueDTO(
    val location: String,
    val email: String,
    val category: String,
    val subCategory: String?,
    val subSubCategory: String?,
    val description: String?,
)

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
}
