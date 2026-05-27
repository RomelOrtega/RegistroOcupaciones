package edu.ucne.registroocupaciones.domain.horaextra.model

class HoraExtra (
    val horaExtraId: Int =0,
    val empleadoId: Int,
    val fecha: Long,
    val cantidadHoras: Double,
    val esNocturna: Boolean,
    val horaAl35: Double = 0.0,
    val horasAl100: Double = 0.0,
    val montoTotal : Double =0.0

)