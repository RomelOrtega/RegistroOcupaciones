package edu.ucne.registroocupaciones.domain.horaextra.repository

import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import kotlinx.coroutines.flow.Flow

interface HoraExtraRepository {

    fun observeHorasExtras(): Flow<List<HoraExtra>>

    suspend fun getHoraExtra(id: Int): HoraExtra?

    suspend fun upsert(horaExtra: HoraExtra): Int

    suspend fun delete(id: Int)

}