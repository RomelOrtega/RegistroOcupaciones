package edu.ucne.registroocupaciones.data.mapper

import edu.ucne.registroocupaciones.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado

fun EmpleadoEntity.toDomain(): Empleado =
    Empleado(
        empleadoId = empleadoId,
        fechaIngreso = fechaIngreso,
        nombres = nombres,
        sexo = sexo,
        sueldo = sueldo
    )
fun Empleado.toEmpleadoEntity(): EmpleadoEntity =
    EmpleadoEntity(
        empleadoId = empleadoId,
        fechaIngreso = fechaIngreso,
        nombres = nombres,
        sexo = sexo,
        sueldo = sueldo

    )