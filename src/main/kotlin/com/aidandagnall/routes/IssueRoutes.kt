package com.aidandagnall.routes

import com.aidandagnall.Constants
import com.aidandagnall.Permissions
import com.aidandagnall.dao.IssueDAOImpl
import com.aidandagnall.models.Issue
import com.aidandagnall.models.IssueDTO
import com.aidandagnall.models.IssueStatus
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.format.DateTimeFormatter

fun Routing.issueRouting() {
   val dao = IssueDAOImpl()
   route("/issue") {
      authenticate(Permissions.READ_ISSUES) {
         get {
               call.respond(dao.allIssues().map { transaction { IssueDTO.fromIssue(it) } })
         }
      }
      authenticate(Permissions.CREATE_ISSUE) {
         post {
            call.principal<JWTPrincipal>()?.subject?.let{ email ->
               val issue = dao.createIssue(
                  call.receive(), email
               )
               call.respond(HttpStatusCode.Created, IssueDTO.fromIssue(issue))
               sendIssueEmail(issue)
            }
            call.respond(HttpStatusCode.BadRequest)
         }
      }

      authenticate(Permissions.EDIT_ISSUE) {
         post("/{id}/complete") {
            call.principal<JWTPrincipal>()?.subject?.let { userId ->
               dao.completeIssue(Integer.parseInt(call.parameters["id"]), userId)?.let {
                  call.respond(HttpStatusCode.OK)
                  launch {sendIssueEmail(it) }
               }
            }
         }

         post("/{id}/in-progress") {
            call.principal<JWTPrincipal>()?.subject?.let { userId ->
               dao.markIssueInProgress(Integer.parseInt(call.parameters["id"]), userId)?.let {
                  call.respond(HttpStatusCode.OK)
               }
            }
         }
      }


      authenticate(Permissions.DELETE_ISSUE) {
         delete("/{id}") {
            val id = call.parameters["id"]
            dao.deleteIssue(Integer.parseInt(id))
            call.respond(HttpStatusCode.OK)
         }
      }
   }

}

suspend fun sendIssueEmail(issue: Issue) {
   val client = HttpClient()
   val template: String = transaction {
      return@transaction when(issue.status) {
         IssueStatus.NEW -> Constants.EMAIL_ISSUE_CREATED_TEMPLATE
         IssueStatus.RESOLVED -> Constants.EMAIL_ISSUE_COMPLETE_TEMPLATE
         else -> null
      }
   } ?: return

   val variables = transaction {
      mapOf<String, String>(
         "username" to issue.email.substringBefore('@'),
         "formattedroom" to Issue.locationCodeToRoomName(issue.location),
         "adminusername" to (issue.closedBy?.email?.substringBefore('@') ?: ""),
         "categorymain" to issue.category,
         "maincategory" to issue.category,
         "categorysub" to (issue.subCategory ?: ""),
         "categorysubsub" to (issue.subSubCategory ?: ""),
         "description" to (issue.description ?: ""),
         "reportdate" to issue.dateSubmitted.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
         "date" to issue.dateSubmitted.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
         "time" to issue.dateSubmitted.format(DateTimeFormatter.ofPattern("HH:mm")),
         "locationid" to issue.location,
         "issueID" to "${issue.id}"
      )
   }

   val response = client.submitForm(Constants.EMAIL_URL) {
      basicAuth("api", Constants.EMAIL_KEY)
      formData {
         parameter("from", Constants.EMAIL_SENDER)
         parameter("to", issue.email)
         parameter("subject", "[#${issue.id}] Lab Monitor: Issue " + if(issue.status == IssueStatus.NEW) "Confirmation" else "Resolved")
         parameter("template", template)
         parameter(
            "h:X-Mailgun-Variables", ObjectMapper().writeValueAsString(variables)
         )
      }
   }
}
