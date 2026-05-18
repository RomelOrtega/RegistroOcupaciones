package edu.ucne.registroocupaciones.domain.Empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository,
    private val validateEmpleadoUseCase: ValidateEmpleadoUseCase
) {
    suspend operator fun invoke(empleado: Empleado): Result<Int> {
        return try {
            val validation = validateEmpleadoUseCase(
                fechaIngreso = empleado.fechaIngreso,
                nombres = empleado.nombres,
                sexo = empleado.sexo,
                sueldo = empleado.sueldo
            )
            if (!validation.isValid){
                val errorMsg = validation.nombresError?: validation.fechaIngresoError
                ?: validation.sexoError ?: validation.sueldoError?: "Error de validacion"
                Result.failure(IllegalArgumentException(errorMsg))
            } else {
                val id = repository.upsert(empleado)
                Result.success(id)
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}