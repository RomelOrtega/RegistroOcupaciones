package edu.ucne.registroocupaciones.domain.horaextra.usecase

import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import javax.inject.Inject

class DeleteHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(id: Int) {
        if (id <= 0) throw IllegalArgumentException("El ID debe ser mayor que 0")
        repository.delete(id)
    }
}