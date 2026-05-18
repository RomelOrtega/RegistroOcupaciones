package edu.ucne.registroocupaciones.domain.empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.Empleado.usecase.GetEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetEmpleadoUseCaseTest {

    private lateinit var repository: EmpleadoRepository
    private lateinit var getEmpleadoUseCase: GetEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        getEmpleadoUseCase = GetEmpleadoUseCase(repository)
    }

    @Test
    fun `retorna empleado cuando existe`() = runTest {

        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Sandra Mejia",
            sexo = "Femenino",
            sueldo = 40000.0
        )

        coEvery {
            repository.getEmpleado(1)
        } returns empleado

        val result = getEmpleadoUseCase(1)

        assertNotNull(result)
        assertEquals("Sandra Mejia", result?.nombres)
    }

    @Test
    fun `retorna null cuando no existe`() = runTest {

        coEvery {
            repository.getEmpleado(99)
        } returns null

        val result = getEmpleadoUseCase(99)

        assertNull(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion cuando id es 0`() = runTest {

        getEmpleadoUseCase(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion cuando id es negativo`() = runTest {

        getEmpleadoUseCase(-1)
    }
}