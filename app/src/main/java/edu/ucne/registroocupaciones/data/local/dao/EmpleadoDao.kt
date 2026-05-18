package edu.ucne.registroocupaciones.data.local.dao

import androidx.room.Query
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import edu.ucne.registroocupaciones.data.local.entities.EmpleadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Query("Select * from empleados order by empleadoId desc")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query("Select * from empleados where empleadoId = :id")
    suspend fun getById(id: Int): EmpleadoEntity?

    @Upsert
    suspend fun upsert(empleado: EmpleadoEntity): Long

    @Delete
    suspend fun delete(empleado: EmpleadoEntity)

    @Query("delete from empleados where empleadoId = :id")
    suspend fun deletebyId(id: Int)

    @Query ("select * from empleados where nombres = :nombre")
    suspend fun getEempleadoByNombre(nombre: String): List<EmpleadoEntity>
}
