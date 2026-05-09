package edu.ucne.registroocupaciones.domain.Ocupaciones.repository

import edu.ucne.registroocupaciones.domain.Ocupaciones.model.Ocupacion
import kotlinx.coroutines.flow.Flow

interface OcupacionRepository {

    fun observeOcupaciones(): Flow<List<Ocupacion>>

    suspend fun getOcupacion(id: Int): Ocupacion?

    suspend fun upsert(ocupacion: Ocupacion): Int

    suspend fun delete(id: Int)

    suspend fun getOcupacionesByDescripcion(descripcion: String): List<Ocupacion>
}