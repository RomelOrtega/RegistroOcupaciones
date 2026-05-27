package edu.ucne.registroocupaciones.presentation.horaextra.list

import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra

data class HoraExtraListState (
    val isLoading: Boolean = false,
    val horasExtras: List<HoraExtra> = emptyList(),
    val empleadoNombres: Map<Int, String> = emptyMap(),
    val message: String? = null
)