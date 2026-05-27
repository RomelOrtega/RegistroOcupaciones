package edu.ucne.registroocupaciones.domain.horaextra.usecase

import javax.inject.Inject

class ValidateHoraExtraUseCase  @Inject constructor(){

    data class ValidationResult(
        val isValid: Boolean,
        val empleadoError: String? = null,
        val fechaError: String? = null,
        val cantidadHorasError: String? = null
    )

    operator fun invoke(
        empleadoId: Int?,
        fecha: Long?,
        cantidadHoras: Double?
    ): ValidationResult {

        val empleadoError = when {
            empleadoId == null || empleadoId <= 0 -> "Debe seleccionar un empleado"
            else -> null
        }

        val fechaError = when {
            fecha == null || fecha <= 0L -> "La fecha es requerida"
            else -> null
        }

        val cantidadHorasError = when {
            cantidadHoras == null || cantidadHoras <= 0.0 -> "La cantidad de horas es requerida y debe ser mayor a 0"
            else -> null
        }

        return ValidationResult(
            isValid = empleadoError == null && fechaError == null && cantidadHorasError == null,
            empleadoError = empleadoError,
            fechaError = fechaError,
            cantidadHorasError = cantidadHorasError
        )
    }
}