package edu.ucne.registroocupaciones.presentation.horaextra.edit

sealed interface HoraExtraEditUIEvent {
    data class Load(val id: Int?) : HoraExtraEditUIEvent
    data class EmpleadoChanged(val empleadoId: Int, val nombre: String) : HoraExtraEditUIEvent
    data class FechaChanged(val value: Long) : HoraExtraEditUIEvent
    data class CantidadHorasChanged(val value: String) : HoraExtraEditUIEvent
    data class TipoHoraExtraChanged(val value: String) : HoraExtraEditUIEvent
    data object Save : HoraExtraEditUIEvent
    data object Delete : HoraExtraEditUIEvent
    data object ShowDatePicker : HoraExtraEditUIEvent
    data object HideDatePicker : HoraExtraEditUIEvent
}