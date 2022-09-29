package com.aidandagnall.dao

import com.aidandagnall.Constants
import com.aidandagnall.Permissions
import com.aidandagnall.models.User
import com.aidandagnall.models.UserPermission
import com.aidandagnall.models.Users
import com.auth0.client.auth.AuthAPI
import com.auth0.client.mgmt.ManagementAPI
import com.auth0.client.mgmt.filter.UserFilter
import com.auth0.exception.APIException
import com.auth0.exception.Auth0Exception
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.NoSuchElementException

class UserDAOImpl : UserDAO {
    override suspend fun getUser(_userId: String): User? = transaction {
        User.findById(_userId)

        val usersByEmail = User.find { Users.email eq _userId }
        if (usersByEmail.count() > 0) usersByEmail.first()

        try {
            User.find { Users.email like "$_userId@%" }.first()
        } catch (e: NoSuchElementException) {
            null
        }
    }
    override suspend fun checkUserPermission(_email: String, _permission: String): Boolean = newSuspendedTransaction{
        val user = User.findById(_email) ?: addUser(_email, Permissions.USER_PERMISSIONS)
        ?: return@newSuspendedTransaction false
        user.permissions.map { it.permission }.contains(_permission)
    }

    override suspend fun addUser(_userId: String, _permissions: List<String>): User? {
        val auth = AuthAPI(Constants.AUTH0_CLIENT_URL, Constants.AUTH0_CLIENT_ID, Constants.AUTH0_CLIENT_SECRET)
        val holder = auth.requestToken(Constants.AUTH0_CLIENT_AUDIENCE).execute()
        val mgmt = ManagementAPI(Constants.AUTH0_CLIENT_URL, holder.accessToken)
        val request = mgmt.users().get(_userId, UserFilter())
        try {
            val auth0_user = request.execute()
            val user = transaction {
                User.new(_userId) {
                    email = auth0_user.email
                    permissions = SizedCollection()
                }
            }
            transaction {
                user.permissions = SizedCollection(_permissions.map { UserPermission.new { permission = it } })
            }
            return user
        } catch (e: APIException) {
            println(e)
            return null
        } catch (e: Auth0Exception) {
            println(e)
            return null
        }
    }

    override suspend fun updateUser(_email: String, _permissions: List<String>) = transaction {
        val user = User.findById(_email)
        val permissions = user?.permissions ?: return@transaction
        user.permissions = SizedCollection(permissions + _permissions.map {
                UserPermission.new { permission = it }
            }
        )
    }

    override suspend fun removePermissionFromUser(_userId: String, _permission: String): Unit = transaction {
        val user = User.findById(_userId)
        user?.let { u ->
            u.permissions = SizedCollection(user.permissions.filter { it.permission != _permission })
        }
    }
    override suspend fun addPermissionToUser(_userId: String, _permission: String): Unit = transaction {
        val user = User.findById(_userId)
        user?.let { u ->
            user.permissions = SizedCollection(user.permissions + UserPermission.new { permission = _permission })
        }
    }
}