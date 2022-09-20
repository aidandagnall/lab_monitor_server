package com.aidandagnall.dao

import com.aidandagnall.models.User

interface UserDAO {
    suspend fun checkUserPermission(_email: String, _permission: String) : Boolean
    suspend fun addUser(_email: String, _permissions: List<String>) : User
    suspend fun updateUser(_email: String, _permissions: List<String>)
}