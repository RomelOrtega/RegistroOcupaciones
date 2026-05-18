package edu.ucne.registroocupaciones.domain.Empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import javax.inject.Inject

class GetEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(id: Int): Empleado? {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getEmpleado(id)
    }
}