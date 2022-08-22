package com.aidandagnall.models

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId

data class Module(
    val _id: Id<Module> = ObjectId().toId(),
    val code: String,
    val name: String,
    val convenor: List<String>,
)