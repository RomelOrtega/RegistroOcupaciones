package edu.ucne.registroocupaciones.presentation.empleado.list

sealed interface EmpleadoListEvent {
    data object Load : EmpleadoListEvent
    data class Delete(val id: Int) : EmpleadoListEvent
    data object CreateNew : EmpleadoListEvent
    data class Edit(val id: Int) : EmpleadoListEvent
}