package com.aidandagnall.dao

import com.aidandagnall.models.User

interface UserDAO {
    suspend fun getUser(_userId: String): User?
    suspend fun checkUserPermission(_email: String, _permission: String) : Boolean
    suspend fun addUser(_userId: String, _permissions: List<String>) : User?
    suspend fun updateUser(_email: String, _permissions: List<String>)
    suspend fun removePermissionFromUser(_userId: String, _permission: String)
    suspend fun addPermissionToUser(_userId: String, _permission: String)
}