package edu.ucne.registroocupaciones.domain.empleado.usecase

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.Empleado.usecase.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ValidateEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpsertEmpleadoUseCaseTest {

    private lateinit var repository: EmpleadoRepository
    private lateinit var validateUseCase: ValidateEmpleadoUseCase
    private lateinit var upsertEmpleadoUseCase: UpsertEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        validateUseCase = mockk()
        upsertEmpleadoUseCase = UpsertEmpleadoUseCase(repository, validateUseCase)
    }

    @Test
    fun `guarda empleado cuando validacion es exitosa`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 1700000000000L,
            nombres = "Gabriel Pena",
            sexo = "Masculino",
            sueldo = 29000.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = true
        )

        coEvery {
            repository.upsert(empleado)
        } returns 1

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())

        coVerify {
            repository.upsert(empleado)
        }
    }

    @Test
    fun `retorna failure cuando nombre esta vacio`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 1700000000000L,
            nombres = "",
            sexo = "Masculino",
            sueldo = 29000.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            nombresError = "El nombre es requerido"
        )

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isFailure)

        coVerify(exactly = 0) {
            repository.upsert(any())
        }
    }

    @Test
    fun `retorna failure cuando sueldo es invalido`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 1700000000000L,
            nombres = "Veronica Guzman",
            sexo = "Femenino",
            sueldo = -500.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            sueldoError = "El sueldo es requerido y debe ser mayor a 0"
        )

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isFailure)

        coVerify(exactly = 0) {
            repository.upsert(any())
        }
    }

    @Test
    fun `retorna failure cuando fecha es invalida`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 0L,
            nombres = "Veronica Guzman",
            sexo = "Femenino",
            sueldo = 30000.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            fechaIngresoError = "La fecha de ingreso es requerida"
        )

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isFailure)

        coVerify(exactly = 0) {
            repository.upsert(any())
        }
    }

    @Test
    fun `actualiza empleado existente correctamente`() = runTest {

        val empleado = Empleado(
            empleadoId = 3,
            fechaIngreso = 1700000000000L,
            nombres = "Ernesto Bautista",
            sexo = "Masculino",
            sueldo = 48000.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = true
        )

        coEvery {
            repository.upsert(empleado)
        } returns 3

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull())
    }

    @Test
    fun `retorna failure cuando repository lanza excepcion`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 1700000000000L,
            nombres = "Carmen Polanco",
            sexo = "Femenino",
            sueldo = 35000.0
        )

        every {
            validateUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = true
        )

        coEvery {
            repository.upsert(empleado)
        } throws Exception("Error de BD")

        val result = upsertEmpleadoUseCase(empleado)

        assertTrue(result.isFailure)
    }
}