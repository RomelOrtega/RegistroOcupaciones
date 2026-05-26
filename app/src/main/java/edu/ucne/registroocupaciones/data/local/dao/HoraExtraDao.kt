package edu.ucne.registroocupaciones.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registroocupaciones.data.local.entities.HoraExtraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraExtraDao {
    @Query("SELECT * FROM horas_extras ORDER BY horaExtraId DESC")
    fun observeAll(): Flow<List<HoraExtraEntity>>

    @Query("SELECT * FROM horas_extras WHERE horaExtraId = :id")
    suspend fun getById(id: Int): HoraExtraEntity?

    @Upsert
    suspend fun upsert(horaExtra: HoraExtraEntity): Long

    @Query("DELETE FROM horas_extras WHERE horaExtraId = :id")
    suspend fun deleteById(id: Int)
}