package edu.ucne.registroocupaciones.presentation.horaextra.list

sealed interface HoraExtraListEvent {
    data object Load : HoraExtraListEvent
    data class Delete(val id: Int) : HoraExtraListEvent
    data object CreateNew : HoraExtraListEvent
    data class Edit(val id: Int) : HoraExtraListEvent
}