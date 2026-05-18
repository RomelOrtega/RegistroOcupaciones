package edu.ucne.registroocupaciones.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupaciones.data.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.local.entities.OcupacionEntity
import edu.ucne.registroocupaciones.data.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.entities.EmpleadoEntity

@Database(
    entities = [
        OcupacionEntity::class,
        EmpleadoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class OcupacionDb : RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun empleadoDao(): EmpleadoDao
}