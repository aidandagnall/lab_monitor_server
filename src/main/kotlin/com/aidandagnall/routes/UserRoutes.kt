package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.Permissions
import com.aidandagnall.dao.UserDAOImpl
import com.aidandagnall.models.User
import com.auth0.client.auth.AuthAPI
import com.auth0.client.mgmt.ManagementAPI
import com.auth0.client.mgmt.filter.UserFilter
import com.auth0.exception.APIException
import com.auth0.exception.Auth0Exception
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction


fun Routing.userRouting() {
    val dao = UserDAOImpl();
    authenticate(Permissions.EDIT_USER) {
        post("/user/{id}/addrole/{role}") {
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
    }

    authenticate {
        get("/user/permissions") {
            val permissions = transaction {
                call.principal<JWTPrincipal>()?.subject?.let { it1 -> User.findById(it1)?.permissions?.toList() }
            }
            if (permissions == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            call.respond(HttpStatusCode.OK, permissions.map { it.permission })
        }
    }

}