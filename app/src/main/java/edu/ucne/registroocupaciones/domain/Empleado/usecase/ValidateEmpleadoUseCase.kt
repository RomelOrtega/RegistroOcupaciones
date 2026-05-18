package edu.ucne.registroocupaciones.domain.Empleado.usecase

import javax.inject.Inject

class ValidateEmpleadoUseCase  @Inject constructor(){

    data class ValidationResult(
        val isValid: Boolean,
        val fechaIngresoError: String? = null,
        val nombresError: String? = null,
        val sexoError: String? = null,
        val sueldoError: String?= null
    )
    operator fun invoke(
        fechaIngreso: Long,
        nombres: String,
        sexo: String,
        sueldo: Double?
    ): ValidationResult{
        val fechaIngresoError = when {
            fechaIngreso == null || fechaIngreso <= 0L -> "La fecha es obligatoria"
            else -> null
        }
        val nombresError = when {
            nombres.isBlank()-> "El nombre es obligatorio"
            else -> null
        }
        val sexoError = when{
            sexo.isBlank()-> "El sexo es obligatorio"
            else -> null
        }
        val sueldoError = when {
            sueldo == null || sueldo <=0.0 -> "El sueldo es obligatorio y debe ser mayor a 0"
            else -> null
        }
        return ValidationResult(
            isValid = fechaIngresoError == null && nombresError == null &&
                    sexoError== null && sueldoError == null,
            fechaIngresoError = fechaIngresoError,
            nombresError = nombresError,
            sexoError = sexoError,
            sueldoError = sueldoError
        )
    }
}