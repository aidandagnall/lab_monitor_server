package com.aidandagnall.dao

import com.aidandagnall.models.*
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = Dotenv.load()["DB_URL"]
        val database = Database.connect(jdbcURL, driverClassName, user = "root")
        transaction(database) {
            SchemaUtils.create(Issues)
            SchemaUtils.create(Rooms)
            SchemaUtils.create(Modules)
            SchemaUtils.create(Labs)
            SchemaUtils.create(LabRooms)
            SchemaUtils.create(Reports)
            SchemaUtils.create(Users)
            SchemaUtils.create(UserPermissions)
            SchemaUtils.create(UserPermissionsConnection)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}