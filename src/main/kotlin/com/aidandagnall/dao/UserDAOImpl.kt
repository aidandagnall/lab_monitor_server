package com.aidandagnall.dao

import com.aidandagnall.Permissions
import com.aidandagnall.models.Permission
import com.aidandagnall.models.User
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAOImpl : UserDAO {
    override suspend fun checkUserPermission(_email: String, _permission: String): Boolean = newSuspendedTransaction{
        val user = User.findById(_email) ?: addUser(_email, Permissions.USER_PERMISSIONS)
        user.permissions.map { it.name.value }.contains(_permission)
    }

    override suspend fun addUser(_email: String, _permissions: List<String>): User = transaction{
        User.new(_email) {
            permissions = SizedCollection(
                _permissions.map {
                    Permission.new(it) {}
                }
            )
        }
    }

    override suspend fun updateUser(_email: String, _permissions: List<String>) = transaction {
        User.findById(_email)?.permissions = SizedCollection(
            _permissions.map {
                Permission.new(it) { }
            }
        )
    }
}