package com.aidandagnall.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class ModuleDTO(
    val id: Int?,
    val code: String,
    val abbreviation: String,
    val name: String,
    val convenor: List<String>,
) {
    companion object {
        fun fromModule(module: Module): ModuleDTO {
            return ModuleDTO(
                id = module.id.value,
                code = module.code,
                abbreviation = module.abbreviation,
                name = module.name,
                convenor = module.convenor.split(",")
            )
        }
    }
}

class Module(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Module>(Modules)
    var code by Modules.code
    var abbreviation by Modules.abbreviation
    var name by Modules.name
    var convenor by Modules.convenor
}

object Modules : IntIdTable() {
    val code = varchar("code", 8)
    val abbreviation = varchar("abbreviation", 3)
    val name = varchar("name", 200)
    val convenor = varchar("convenor", 100)
}