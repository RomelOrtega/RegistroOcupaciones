package edu.ucne.registroocupaciones.domain.Ocupaciones.usecase

import edu.ucne.registroocupaciones.domain.Ocupaciones.model.Ocupacion
import edu.ucne.registroocupaciones.domain.Ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int): Ocupacion? {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getOcupacion(id)
    }
}