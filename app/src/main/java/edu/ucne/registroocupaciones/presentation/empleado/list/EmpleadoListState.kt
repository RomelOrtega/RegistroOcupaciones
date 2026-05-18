package edu.ucne.registroocupaciones.presentation.empleado.list

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado

data class EmpleadoListState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null
)