package com.aidandagnall.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table

//class UserPermission(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<UserPermission>(UserPermissions)
//    var user by UserPermissions.user
//    var permission by UserPermissions.permission
//}

object UserPermissions : Table() {
    val user = reference("user_id", Users)
    val permission = reference("permission", Permissions)
}

object Permissions : IdTable<String>() {
    override val id = varchar("id", 20).uniqueIndex().entityId()
}

class Permission(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Permission>(Permissions)
    var name by Permissions.id
}
