package com.aidandagnall

import com.aidandagnall.models.UserSession
import com.aidandagnall.plugins.*
import com.google.common.hash.Hashing
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cors.routing.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.nio.charset.StandardCharsets

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        allowHost("lab-monitor.herokuapp.com", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        anyHost()
    }
    install(Authentication) {
        token {
            validate {
                val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
                val sessions = db.getCollection<UserSession>("user_session")
                val session = sessions.findOne { UserSession::tokenHash eq Hashing.sha256().hashString(it.token, StandardCharsets.UTF_8).toString() }
                if (session != null) UserIdPrincipal(session.email)
                else null
            }
        }
    }
    configureRouting()
    configureSerialization()
}
