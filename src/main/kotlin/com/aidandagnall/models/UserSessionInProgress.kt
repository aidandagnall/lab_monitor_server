package com.aidandagnall.models

import java.time.Instant

data class UserSessionInProgress(val email: String, val code: String, val tokenHash: String, val validUntil: Instant)