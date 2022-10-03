package com.aidandagnall.dao

import com.aidandagnall.models.Module
import com.aidandagnall.models.ModuleDTO

interface ModuleDAO {
    suspend fun getModules(): List<Module>
    suspend fun createModule(dto: ModuleDTO): Module
}