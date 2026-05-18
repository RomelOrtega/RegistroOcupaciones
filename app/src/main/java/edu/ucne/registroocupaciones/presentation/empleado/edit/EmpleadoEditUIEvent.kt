package edu.ucne.registroocupaciones.presentation.empleado.edit

sealed interface EmpleadoEditUIEvent {
    data class Load(val id: Int?) : EmpleadoEditUIEvent
    data class FechaIngresoChanged(val value: Long) : EmpleadoEditUIEvent
    data class NombresChanged(val value: String) : EmpleadoEditUIEvent
    data class SexoChanged(val value: String) : EmpleadoEditUIEvent
    data class SueldoChanged(val value: String) : EmpleadoEditUIEvent
    data object Save : EmpleadoEditUIEvent
    data object Delete : EmpleadoEditUIEvent
    data object ShowDatePicker : EmpleadoEditUIEvent
    data object HideDatePicker : EmpleadoEditUIEvent
}