package edu.ucne.registroocupaciones.presentation.horaextra.edit

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado

data class HoraExtraEditUIState (
    val horaExtraId: Int? = null,
    val empleadoId: Int? = null,
    val empleadoNombre: String = "",
    val fecha: Long? = null,
    val cantidadHoras: Double? = null,
    val tipoHoraExtra: String = "",
    val empleados: List<Empleado> = emptyList(),
    val sueldoPorDia: Double = 0.0,
    val sueldoPorHora: Double = 0.0,
    val horasAl35: Double = 0.0,
    val horasAl100: Double = 0.0,
    val montoHoras35: Double = 0.0,
    val montoHoras100: Double = 0.0,
    val montoNocturno: Double = 0.0,
    val montoTotal: Double = 0.0,
    val empleadoError: String? = null,
    val fechaError: String? = null,
    val cantidadHorasError: String? = null,
    val tipoHoraExtraError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val showDatePicker: Boolean = false
)