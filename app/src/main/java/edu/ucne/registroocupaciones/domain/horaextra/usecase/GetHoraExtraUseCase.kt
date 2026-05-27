package edu.ucne.registroocupaciones.domain.horaextra.usecase

import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import javax.inject.Inject

class GetHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(id: Int): HoraExtra? {
        if (id <= 0) throw IllegalArgumentException("El id debe de ser mayor a 0")
        return repository.getHoraExtra(id)
    }
}