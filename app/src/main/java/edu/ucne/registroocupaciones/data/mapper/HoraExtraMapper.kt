package edu.ucne.registroocupaciones.data.mapper

import edu.ucne.registroocupaciones.data.local.entities.HoraExtraEntity
import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra

fun HoraExtraEntity.toDomain(): HoraExtra =
    HoraExtra(
        horaExtraId = horaExtraId,
        empleadoId = empleadoId,
        fecha = fecha,
        cantidadHoras = cantidadHoras,
        esNocturna = esNocturna,
        horasAl35,
        horasAl100 = horasAl100,
        montoTotal = montoTotal
    )

fun HoraExtra.toHoraExtraEntity(): HoraExtraEntity =
    HoraExtraEntity(
        horaExtraId = horaExtraId,
        empleadoId = empleadoId,
        fecha = fecha,
        cantidadHoras = cantidadHoras,
        esNocturna = esNocturna,
        horasAl35 = horaAl35,
        horasAl100 = horasAl100,
        montoTotal = montoTotal
    )