package com.aidandagnall

import io.github.cdimascio.dotenv.dotenv

class Constants {
    companion object {
        val JWT_ISSUER: String = dotenv()["JWT_ISSUER"]
        val JWT_AUDIENCE: String = dotenv()["JWT_AUDIENCE"]

        val AUTH0_CLIENT_ID: String = dotenv()["AUTH0_CLIENT_ID"]
        val AUTH0_CLIENT_SECRET: String = dotenv()["AUTH0_CLIENT_SECRET"]
        val AUTH0_CLIENT_URL: String = dotenv()["AUTH0_CLIENT_URL"]
        val AUTH0_CLIENT_AUDIENCE: String = dotenv()["AUTH0_CLIENT_AUDIENCE"]

        val EMAIL_URL: String = dotenv()["EMAIL_URL"]
        val EMAIL_KEY: String = dotenv()["EMAIL_KEY"]
        val EMAIL_SENDER: String = dotenv()["EMAIL_SENDER"]
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
        const val CREATE_LAB: String = "create:lab"
        const val EDIT_USER: String = "edit:user"
        const val DELETE_USER: String = "delete:user"
        const val DELETE_LAB: String = "delete:lab"
        const val CREATE_MODULE: String = "create:module"
        const val DELETE_MODULE: String = "delete:module"

        val ALL = listOf(
            READ_ROOMS,
            CREATE_ISSUE,
            CREATE_REPORT,
            READ_ISSUES,
            READ_REPORTS,
            EDIT_ISSUE,
            DELETE_ISSUE,
            DELETE_REPORT,
            CREATE_LAB,
            EDIT_USER,
            DELETE_USER,
            DELETE_LAB,
            CREATE_MODULE,
            DELETE_MODULE
        )

        val USER_PERMISSIONS = listOf(
            READ_ROOMS,
            CREATE_ISSUE,
            CREATE_REPORT
        )

        val RST_PERMISSIONS = listOf(
            READ_ISSUES,
            DELETE_ISSUE,
            EDIT_ISSUE,
        )

        val ADMIN_PERMISSIONS = listOf(
            CREATE_LAB,
            DELETE_LAB,
            READ_REPORTS,
            DELETE_REPORT,
            EDIT_USER,
            DELETE_USER,
            CREATE_MODULE,
            DELETE_MODULE
        )

    }
}