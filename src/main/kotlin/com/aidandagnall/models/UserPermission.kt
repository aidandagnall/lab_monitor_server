package com.aidandagnall.models

import com.aidandagnall.Permissions
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

//class UserPermission(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<UserPermission>(UserPermissions)
//    var user by UserPermissions.user
//    var permission by UserPermissions.permission
//}

object UserPermissionsConnection : Table() {
    val user = reference("user_id", Users)
    val permission = reference("permission", UserPermissions)
}

object UserPermissions : IntIdTable() {
    val permission = varchar("permission", 15)
}

class UserPermission(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserPermission>(UserPermissions)
    var permission by UserPermissions.permission
}
