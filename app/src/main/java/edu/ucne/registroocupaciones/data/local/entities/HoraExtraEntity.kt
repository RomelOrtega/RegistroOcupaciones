package edu.ucne.registroocupaciones.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "horas_extras",
    foreignKeys = [
        ForeignKey(
            entity = EmpleadoEntity::class,
            parentColumns = ["empleadoId"],
            childColumns = ["empleadoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraId: Int = 0,
    val empleadoId: Int,
    val fecha: Long,
    val cantidadHoras: Double,
    val esNocturna: Boolean,
    val horasAl35: Double,
    val horasAl100: Double,
    val montoTotal: Double
)