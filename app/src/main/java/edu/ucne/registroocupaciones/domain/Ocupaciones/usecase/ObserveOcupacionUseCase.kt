package edu.ucne.registroocupaciones.domain.Ocupaciones.usecase

import edu.ucne.registroocupaciones.domain.Ocupaciones.model.Ocupacion
import edu.ucne.registroocupaciones.domain.Ocupaciones.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> {
        return repository.observeOcupaciones()
    }
}