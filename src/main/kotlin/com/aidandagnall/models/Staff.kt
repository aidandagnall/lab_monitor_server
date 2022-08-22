package com.aidandagnall.models

import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Staff(
    val _id: Id<Staff> = newId(),
    val name: String)