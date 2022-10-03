package com.aidandagnall.dao

import com.aidandagnall.models.Module
import com.aidandagnall.models.ModuleDTO
import org.jetbrains.exposed.sql.transactions.transaction

class ModuleDAOImpl : ModuleDAO {
    override suspend fun getModules(): List<Module> = transaction {
        Module.all().toList()
    }

    override suspend fun createModule(dto: ModuleDTO): Module = transaction {
        Module.new {
            code = dto.code
            abbreviation = dto.abbreviation
            convenor = dto.convenor.joinToString(", ")
            name = dto.name
        }
    }

}