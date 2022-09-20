package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class ModuleDTO(
    val code: String,
    val abbreviation: String,
    val name: String,
    val convenor: List<String>,
)

class Module(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Module>(Modules)
    var code by Modules.code
    var abbreviation by Modules.abbreviation
    var name by Modules.name
    var convenor by Modules.convenor
}

object Modules : IntIdTable() {
    val code = varchar("code", 7)
    val abbreviation = varchar("abbreviation", 3)
    val name = varchar("name", 50)
    val convenor = varchar("convenor", 30)
}