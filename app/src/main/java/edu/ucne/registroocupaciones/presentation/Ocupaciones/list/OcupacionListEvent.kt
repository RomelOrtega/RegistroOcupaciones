package edu.ucne.registroocupaciones.presentation.Ocupaciones.list

sealed interface OcupacionListEvent {
    data object Load : OcupacionListEvent
    data class Delete(val id: Int) : OcupacionListEvent
    data object CreateNew : OcupacionListEvent
    data class Edit(val id: Int) : OcupacionListEvent
    data class ShowMessage(val message: String) : OcupacionListEvent
}