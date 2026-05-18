package edu.ucne.registroocupaciones.domain.Empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    operator fun invoke(): Flow<List<Empleado>>{
        return repository.observeEmpleados()
    }
}