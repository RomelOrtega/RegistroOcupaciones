package edu.ucne.registroocupaciones.domain.Ocupaciones.usecase

import edu.ucne.registroocupaciones.domain.Ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class ValidateOcupacionUseCase @Inject constructor(
    private val ocupacionRepository: OcupacionRepository
) {
    data class ValidationResult(
        val isValid: Boolean,
        val descripcionError: String? = null,
        val sueldoError: String? = null
    )

    suspend operator fun invoke(
        descripcion: String,
        sueldo: Double?,
        currentOcupacionId: Int? = null
    ): ValidationResult {val descripcionError = when {
        descripcion.isBlank() -> "La descripción es requerida"
        else -> {
            val existingOcupaciones = ocupacionRepository.getOcupacionesByDescripcion(descripcion)
            val isDuplicate = if (currentOcupacionId != null) {
                existingOcupaciones.any { it.ocupacionId != currentOcupacionId }
            } else {
                existingOcupaciones.isNotEmpty()
            }
            if (isDuplicate) "Ya existe una ocupación registrada con esta descripción" else null
        }
    }

        val sueldoError = when {
            sueldo == null || sueldo <= 0.0 -> "El sueldo es requerido y debe ser mayor a 0"
            else -> null
        }

        return ValidationResult(
            isValid = descripcionError == null && sueldoError == null,
            descripcionError = descripcionError,
            sueldoError = sueldoError
        )
    }
}