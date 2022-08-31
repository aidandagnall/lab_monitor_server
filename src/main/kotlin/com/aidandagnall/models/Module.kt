package com.aidandagnall.models

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId

data class Module(
    val _id: Id<Module> = ObjectId().toId(),
    val code: String,
    val abbreviation: String,
    val name: String,
    val convenor: List<String>,
)

data class ModuleDTO(
    val code: String,
    val abbreviation: String,
    val name: String,
    val convenor: List<String>,
)