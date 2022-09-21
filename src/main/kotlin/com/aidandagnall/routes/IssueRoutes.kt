package com.aidandagnall.routes

import com.aidandagnall.Permissions
import com.aidandagnall.dao.IssueDAOImpl
import com.aidandagnall.models.IssueDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.issueRouting() {
   val dao = IssueDAOImpl()
   route("/issue") {
      authenticate(Permissions.READ_ISSUES) {
         get {
               call.respond(dao.allIssues())
         }
      }
      authenticate(Permissions.CREATE_ISSUE) {
         post {
            call.principal<JWTPrincipal>()?.subject?.let{ email ->
               val issue = dao.createIssue(
                  call.receive(), email
               )
               call.respond(HttpStatusCode.Created, IssueDTO.fromIssue(issue))
            }
            call.respond(HttpStatusCode.BadRequest)
         }
      }
   }
}