package com.aidandagnall.models

import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class LabExpanded(
    val _id: Id<Lab> = newId(),
    val module: Module,
    val day: Int,
    val startTime: String,
    val endTime: String,
    val removalChance: RemovalChance
)
