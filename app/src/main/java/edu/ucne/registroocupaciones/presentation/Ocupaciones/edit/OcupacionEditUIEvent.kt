package edu.ucne.registroocupaciones.presentation.Ocupaciones.edit

sealed interface OcupacionEditUIEvent {
    data class Load(val id: Int?) : OcupacionEditUIEvent
    data class DescripcionChanged(val value: String) : OcupacionEditUIEvent
    data class SueldoChanged(val value: String) : OcupacionEditUIEvent
    data object Save : OcupacionEditUIEvent
    data object Delete : OcupacionEditUIEvent
}