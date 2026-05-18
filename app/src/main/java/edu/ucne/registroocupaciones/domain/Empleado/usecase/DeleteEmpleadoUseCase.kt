package edu.ucne.registroocupaciones.domain.Empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import javax.inject.Inject

class DeleteEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(id: Int){
        if (id <= 0) throw IllegalArgumentException("El Id debe de ser mayor a 0")
        repository.delete(id)
    }
}
