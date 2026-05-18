package edu.ucne.registroocupaciones.domain.empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.Empleado.usecase.DeleteEmpleadoUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteEmpleadoUseCaseTest {

    private lateinit var repository: EmpleadoRepository
    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        deleteEmpleadoUseCase = DeleteEmpleadoUseCase(repository)
    }

    @Test
    fun `elimina empleado correctamente`() = runTest {

        coEvery {
            repository.delete(1)
        } just Runs

        deleteEmpleadoUseCase(1)

        coVerify {
            repository.delete(1)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion cuando id es 0`() = runTest {

        deleteEmpleadoUseCase(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion cuando id es negativo`() = runTest {

        deleteEmpleadoUseCase(-5)
    }
}