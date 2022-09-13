package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.models.Issue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

fun Routing.issueRouting() {
   route("/issue") {
      val db = KMongo.createClient(Constants.CONNECTION_STRING).getDatabase("labs")
      val issues = db.getCollection<Issue>()
      authenticate {

         get {
            if (call.request.headers["key"] == Constants.API_KEY) {
               call.respond(issues.find().toList())
            }
         }
         post {
            val issue: Issue = call.receive()
            issues.insertOne(issue.copy())
            call.respond(HttpStatusCode.Created)
         }
      }
   }
}