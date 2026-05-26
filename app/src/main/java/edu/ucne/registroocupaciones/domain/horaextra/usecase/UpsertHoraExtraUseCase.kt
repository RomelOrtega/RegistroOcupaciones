package edu.ucne.registroocupaciones.domain.horaextra.usecase

import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import javax.inject.Inject

class UpsertHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository,
    private val validateHoraExtraUseCase: ValidateHoraExtraUseCase
) {
    suspend operator fun invoke(horaExtra: HoraExtra): Result<Int> {
        return try {
            val validation = validateHoraExtraUseCase(
                empleadoId = horaExtra.empleadoId,
                fecha = horaExtra.fecha,
                cantidadHoras = horaExtra.cantidadHoras
            )

            if (!validation.isValid) {
                val errorMsg = validation.empleadoError ?: validation.fechaError
                ?: validation.cantidadHorasError ?: "Error de validación"
                Result.failure(IllegalArgumentException(errorMsg))
            } else {
                val id = repository.upsert(horaExtra)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}