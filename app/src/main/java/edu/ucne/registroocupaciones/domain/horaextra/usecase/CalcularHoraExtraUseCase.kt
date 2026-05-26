package edu.ucne.registroocupaciones.domain.horaextra.usecase

import javax.inject.Inject

class CalcularHoraExtraUseCase @Inject constructor() {

    data class ResultadoCalculo(
        val sueldoPorDia: Double,
        val sueldoPorHora: Double,
        val horasAl35: Double,
        val horasAl100: Double,
        val montoHoras35: Double,
        val montoHoras100: Double,
        val montoNocturno: Double,
        val montoTotal: Double
    )

    companion object {
        const val DIAS_LABORABLES_MES = 23.83
        const val HORAS_JORNADA = 8.0
        const val TOPE_SEMANAL_35 = 24.0
        const val PORCENTAJE_35 = 0.35
        const val PORCENTAJE_100 = 1.00
        const val PORCENTAJE_NOCTURNO = 0.15
    }

    operator fun invoke(
        sueldoMensual: Double,
        cantidadHoras: Double,
        tipoHoraExtra: String
    ): ResultadoCalculo {
        val sueldoPorDia = sueldoMensual / DIAS_LABORABLES_MES
        val sueldoPorHora = sueldoPorDia / HORAS_JORNADA

        var horasAl35 = 0.0
        var horasAl100 = 0.0
        var montoHoras35 = 0.0
        var montoHoras100 = 0.0
        var montoNocturno = 0.0

        when (tipoHoraExtra) {
            "Dia" -> {
                horasAl35 = if (cantidadHoras <= TOPE_SEMANAL_35) cantidadHoras else TOPE_SEMANAL_35
                horasAl100 = if (cantidadHoras > TOPE_SEMANAL_35) cantidadHoras - TOPE_SEMANAL_35 else 0.0
                montoHoras35 = horasAl35 * sueldoPorHora * (1 + PORCENTAJE_35)
                montoHoras100 = horasAl100 * sueldoPorHora * (1 + PORCENTAJE_100)
            }
            "Nocturna" -> {
                horasAl35 = if (cantidadHoras <= TOPE_SEMANAL_35) cantidadHoras else TOPE_SEMANAL_35
                horasAl100 = if (cantidadHoras > TOPE_SEMANAL_35) cantidadHoras - TOPE_SEMANAL_35 else 0.0
                montoHoras35 = horasAl35 * sueldoPorHora * (1 + PORCENTAJE_35)
                montoHoras100 = horasAl100 * sueldoPorHora * (1 + PORCENTAJE_100)
                montoNocturno = (montoHoras35 + montoHoras100) * PORCENTAJE_NOCTURNO
            }
            "Día Feriado/Descanso" -> {
                horasAl35 = 0.0
                horasAl100 = cantidadHoras
                montoHoras100 = horasAl100 * sueldoPorHora * (1 + PORCENTAJE_100)
            }
            "Feriado Nocturno" -> {
                horasAl35 = 0.0
                horasAl100 = cantidadHoras
                montoHoras100 = horasAl100 * sueldoPorHora * (1 + PORCENTAJE_100)
                montoNocturno = montoHoras100 * PORCENTAJE_NOCTURNO
            }
        }

        val montoTotal = montoHoras35 + montoHoras100 + montoNocturno

        return ResultadoCalculo(
            sueldoPorDia = sueldoPorDia,
            sueldoPorHora = sueldoPorHora,
            horasAl35 = horasAl35,
            horasAl100 = horasAl100,
            montoHoras35 = montoHoras35,
            montoHoras100 = montoHoras100,
            montoNocturno = montoNocturno,
            montoTotal = montoTotal
        )
    }
}