package com.aidandagnall

import io.github.cdimascio.dotenv.dotenv

class Constants {
    companion object {
        val CONNECTION_STRING: String = dotenv()["CONNECTION_STRING"]
        val API_KEY: String = dotenv()["API_KEY"]
        val EMAIL_AUTH_CODE: String = dotenv()["EMAIL_AUTH_CODE"]
        val ACCEPTED_EMAIL_DOMAINS: List<String> = dotenv()["ACCEPTED_EMAIL_DOMAINS"].split(",")
    }
}