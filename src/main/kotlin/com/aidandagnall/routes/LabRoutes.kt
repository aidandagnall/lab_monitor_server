package com.aidandagnall.routes

import com.aidandagnall.dao.LabDAOImpl
import io.ktor.server.routing.*

fun Route.labRouting() {
    val dao = LabDAOImpl()

    route("/labs") {
    }
}