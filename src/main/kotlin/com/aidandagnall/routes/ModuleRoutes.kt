package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.ModuleDAOImpl
import com.aidandagnall.models.ModuleDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.moduleRouting() {
    val dao = ModuleDAOImpl()
    route("/module") {
        authenticate(Permissions.READ_ROOMS) {
           get {
               call.respond(dao.getModules().map { ModuleDTO(id = it.id.value, code = it.code, abbreviation = it.abbreviation, name = it.name, convenor = it.convenor.split(",")) })
           }
        }

        authenticate(Permissions.CREATE_MODULE) {
            post {
                val module = call.receive<ModuleDTO>()
                dao.createModule(module)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}