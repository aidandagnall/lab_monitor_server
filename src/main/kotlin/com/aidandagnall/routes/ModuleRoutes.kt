package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.Lab
import com.aidandagnall.models.Module
import com.aidandagnall.models.ModuleDTO
import com.aidandagnall.models.Report
import com.aidandagnall.models.Room
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*

fun Route.moduleRouting() {

    route("/module") {
        post {
            if (call.request.headers["key"] == Constants.API_KEY) {
                val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
                val moduleCollection = db.getCollection<com.aidandagnall.models.Module>()
                val dto: ModuleDTO = call.receive()
                val module: Module? = moduleCollection.findOne { Module::code eq dto.code}
                if (module == null) {
                    moduleCollection.insertOne(
                        Module(
                            code = dto.code,
                            abbreviation = dto.abbreviation,
                            name = dto.name,
                            convenor = dto.convenor
                        ))
                }
                else {
                    moduleCollection.updateOneById(module._id, Module(
                        code = dto.code,
                        abbreviation = dto.abbreviation,
                        name = dto.name,
                        convenor = dto.convenor
                    ))
                }
                call.respond("Added")
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}