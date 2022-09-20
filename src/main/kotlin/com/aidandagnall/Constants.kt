package com.aidandagnall

import io.github.cdimascio.dotenv.dotenv

class Constants {
    companion object {
        val JWT_ISSUER: String = dotenv()["JWT_ISSUER"]
        val JWT_AUDIENCE: String = dotenv()["JWT_AUDIENCE"]
    }
}

class Permissions {
    companion object {
        const val READ_ROOMS: String = "read:rooms"
        const val CREATE_ISSUE: String = "create:issue"
        const val CREATE_REPORT: String = "create:report"
        const val READ_ISSUES: String = "read:issues"
        const val READ_REPORTS: String = "read:reports"
        const val EDIT_ISSUE: String = "edit:issue"
        const val DELETE_ISSUE: String = "delete:issue"
        const val DELETE_REPORT: String = "delete:report"

        val ALL = listOf(
            READ_ROOMS,
            CREATE_ISSUE,
            CREATE_REPORT,
            READ_ISSUES,
            READ_REPORTS,
            EDIT_ISSUE,
            DELETE_ISSUE,
            DELETE_REPORT
        )

        val USER_PERMISSIONS = listOf(
            READ_ROOMS,
            CREATE_ISSUE,
            CREATE_REPORT
        )

    }
}