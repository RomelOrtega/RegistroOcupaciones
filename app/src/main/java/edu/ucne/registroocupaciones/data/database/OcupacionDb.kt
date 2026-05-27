package edu.ucne.registroocupaciones.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupaciones.data.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.local.entities.OcupacionEntity
import edu.ucne.registroocupaciones.data.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupaciones.data.local.entities.HoraExtraEntity

@Database(
    entities = [
        OcupacionEntity::class,
        EmpleadoEntity::class,
        HoraExtraEntity:: class
    ],
    version = 3,
    exportSchema = false
)
abstract class OcupacionDb : RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun empleadoDao(): EmpleadoDao
    abstract fun horaExtraDao(): HoraExtraDao
}