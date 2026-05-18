package edu.ucne.registroocupaciones.data.repository

import edu.ucne.registroocupaciones.data.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.mapper.toDomain
import edu.ucne.registroocupaciones.data.mapper.toEmpleadoEntity
import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(
    private val empleadoDao: EmpleadoDao
) : EmpleadoRepository {

    override fun observeEmpleados(): Flow<List<Empleado>> {
        return empleadoDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getEmpleado(id: Int): Empleado? {
        return empleadoDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int {
        val entity = empleado.toEmpleadoEntity()
        val result = empleadoDao.upsert(entity)
        return if (empleado.empleadoId == 0) result.toInt() else empleado.empleadoId
    }

    override suspend fun delete(id: Int) {
        empleadoDao.deletebyId(id)
    }

    override suspend fun getEmpleadosByNombre(nombre: String): List<Empleado> {
        return empleadoDao.getEempleadoByNombre(nombre).map { it.toDomain() }
    }
}