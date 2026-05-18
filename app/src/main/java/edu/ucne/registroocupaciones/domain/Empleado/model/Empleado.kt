package edu.ucne.registroocupaciones.domain.Empleado.model

data class Empleado(
    val empleadoId: Int = 0,
    val fechaIngreso: Long,
    val nombres: String,
    val sexo: String,
    val sueldo: Double
)