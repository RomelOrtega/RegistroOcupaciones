package edu.ucne.registroocupaciones.data.repository

import edu.ucne.registroocupaciones.data.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.data.mapper.toDomain
import edu.ucne.registroocupaciones.data.mapper.toHoraExtraEntity
import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HoraExtraRepositoryImpl @Inject constructor(
    private val horaExtraDao: HoraExtraDao
) : HoraExtraRepository {

    override fun observeHorasExtras(): Flow<List<HoraExtra>> {
        return horaExtraDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHoraExtra(id: Int): HoraExtra? {
        return horaExtraDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(horaExtra: HoraExtra): Int {
        val entity = horaExtra.toHoraExtraEntity()
        val result = horaExtraDao.upsert(entity)
        return if (horaExtra.horaExtraId == 0) result.toInt() else horaExtra.horaExtraId
    }

    override suspend fun delete(id: Int) {
        horaExtraDao.deleteById(id)
    }
}