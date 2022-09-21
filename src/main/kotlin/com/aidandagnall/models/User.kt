package com.aidandagnall.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

class User(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, User>(Users)
    var permissions by UserPermission via UserPermissionsConnection
    var email by Users.email
}

object Users : IdTable<String>() {
    override val id = varchar("id", 30).uniqueIndex().entityId()
    val email = varchar("email", 30).uniqueIndex()
}