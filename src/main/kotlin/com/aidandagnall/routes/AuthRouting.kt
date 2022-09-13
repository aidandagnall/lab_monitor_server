package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.Issue
import com.aidandagnall.models.UserSession
import com.aidandagnall.models.UserSessionInProgress
import com.google.common.hash.Hashing
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*
import kotlin.random.Random

fun Routing.authRouting() {
    route("/auth") {
        val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
        val sessionInProgress = db.getCollection<UserSessionInProgress>("user_session_in_progress")
        val sessions = db.getCollection<UserSession>("user_session")

        post("/email") {
            val email = call.receiveParameters()["email"].toString()
            if (!Constants.ACCEPTED_EMAIL_DOMAINS.any { email.endsWith(it) }) {
                call.respond(HttpStatusCode.NotAcceptable)
                return@post
            }
            val random = SecureRandom.getInstanceStrong()
            val bytes = ByteArray(32)
            random.nextBytes(bytes)

            val token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
            val code = genEmailAuth()
            sessionInProgress.insertOne(UserSessionInProgress(
                email = email,
                code = code,
                tokenHash = Hashing.sha256().hashString(token, StandardCharsets.UTF_8).toString(),
                // valid for 30 minutes after creation
                validUntil = Instant.now().plusSeconds(30 * 60)
            ))

            sendMail(recipient = email, code = code)
            call.respond(HttpStatusCode.Accepted, token)
        }

        post("/code") {
            val token = call.request.authorization()
            if (token == null || !token.startsWith("Bearer ")) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val session = sessionInProgress.findOne{ UserSessionInProgress::tokenHash eq Hashing.sha256().hashString(token.removePrefix("Bearer "), StandardCharsets.UTF_8).toString() }
            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            if (Instant.now() > session.validUntil || session.code != call.receiveParameters()["code"].toString()) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            sessionInProgress.deleteOne(UserSessionInProgress::tokenHash eq session.tokenHash)
            sessions.insertOne(UserSession(email = session.email, tokenHash = session.tokenHash))
            call.respond(HttpStatusCode.Created)

        }

        post("logout") {
            val token = call.request.authorization()
            if (token == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            sessions.deleteOne(UserSession::tokenHash eq Hashing.sha256().hashString(token.removePrefix("Bearer "), StandardCharsets.UTF_8).toString())
            call.respond(HttpStatusCode.Accepted)
        }
    }
}

fun genEmailRef(): String{
    //generate a random string of length 6
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..10)
        .map { allowedChars.random() }
        .joinToString("")
}

fun genEmailAuth(): String{
    //generate a random string of length 6
    val randomValues = List(6) { Random.nextInt(0, 9) }
    return randomValues.joinToString("")
}

suspend fun sendMail(recipient: String, code: String): Boolean {
    //send a get request to unioutlet.co.uk/endpoint.php
    val response: HttpResponse = HttpClient(CIO).request("https://unioutlet.co.uk/endpoint.php") {
        method = HttpMethod.Get
        parameter("auth", Constants.EMAIL_AUTH_CODE)
        parameter("sendto", recipient)
        parameter("ref", genEmailRef())
        parameter("code", code)
    }
    return response.status == HttpStatusCode.OK
}
