package com.aidandagnall

import io.github.cdimascio.dotenv.dotenv

class Constants {
    companion object {
        val CONNECTION_STRING: String = dotenv()["CONNECTION_STRING"]
        val API_KEY: String = dotenv()["API_KEY"]
        val ADMINS: List<String> = dotenv()["ADMINS"].split(",")
    }
}