package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.UserDAOImpl
import com.aidandagnall.models.User
import com.aidandagnall.models.UserDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction


fun Routing.userRouting() {
    val dao = UserDAOImpl()
    route("/user") {

    authenticate(Permissions.EDIT_USER) {
        get("/{id}") {
            val userId = call.parameters["id"]
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val user = dao.getUser(userId)
            if (user != null) {
                call.respond(UserDTO.fromUser(user))
            }
        }
        post("/{id}/addrole/{role}") {
            val userId = call.parameters["id"]
            val role = call.parameters["role"]
            if (userId == null || role == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (role == "admin") {
               dao.updateUser(userId, Permissions.ADMIN_PERMISSIONS)
            }
        }
        post("/{id}/remove-permission/{permission}") {
            val userId = call.parameters["id"]
            val permission = call.parameters["permission"]
            if (userId == null || permission == null || permission !in Permissions.ALL) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            dao.removePermissionFromUser(userId, permission)
            call.respond(HttpStatusCode.Accepted)

        }
        post("/{id}/add-permission/{permission}") {
            val userId = call.parameters["id"]
            val permission = call.parameters["permission"]
            if (userId == null || permission == null || permission !in Permissions.ALL) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            dao.addPermissionToUser(userId, permission)
            call.respond(HttpStatusCode.Accepted)
        }

    }

    authenticate {
        get("/permissions") {
            val permissions = transaction {
                call.principal<JWTPrincipal>()?.subject?.let { it1 -> User.findById(it1)?.permissions?.toList() }
            }
            if (permissions == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            call.respond(HttpStatusCode.OK, mapOf(
                "admin" to permissions.map { it.permission }.any { it !in Permissions.USER_PERMISSIONS },
                "permissions" to permissions.map { it.permission })
            )
        }

        get("/permissions/all") {
            call.respond(Permissions.USER_PERMISSIONS + Permissions.RST_PERMISSIONS + Permissions.ADMIN_PERMISSIONS)
        }
    }
    }


}