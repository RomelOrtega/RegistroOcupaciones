package edu.ucne.registroocupaciones.presentation.Ocupaciones.list

import edu.ucne.registroocupaciones.domain.Ocupaciones.model.Ocupacion

data class OcupacionListState(
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)