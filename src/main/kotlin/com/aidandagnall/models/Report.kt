package com.aidandagnall.models

import org.litote.kmongo.Id
import java.time.Instant
import java.util.*

data class Report(val time: Instant, val room: Id<Room>, val popularity: Popularity?, val removalChance: RemovalChance?, val email: String?)

data class ReportDTO(val room: String, val popularity: Popularity?, val removalChance: RemovalChance?)