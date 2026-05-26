package edu.ucne.registroocupaciones.domain.horaextra.usecase

import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    operator fun invoke(): Flow<List<HoraExtra>>{
        return  repository.observeHorasExtras()
    }
}