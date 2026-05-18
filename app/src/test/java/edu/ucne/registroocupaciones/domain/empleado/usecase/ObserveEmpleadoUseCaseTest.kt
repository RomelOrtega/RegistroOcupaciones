package edu.ucne.registroocupaciones.domain.empleado.usecase

import edu.ucne.registroocupaciones.data.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupaciones.data.repository.EmpleadoRepositoryImpl
import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class EmpleadoRepositoryImplTest {

    private lateinit var dao: EmpleadoDao
    private lateinit var repository: EmpleadoRepositoryImpl

    @Before
    fun setup() {
        dao = mockk()
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `observeEmpleados retorna lista de empleados`() = runTest {

        val entities = listOf(
            EmpleadoEntity(
                empleadoId = 1,
                fechaIngreso = 1700000000000L,
                nombres = "Carlos Ortega",
                sexo = "Masculino",
                sueldo = 35000.0
            ),
            EmpleadoEntity(
                empleadoId = 2,
                fechaIngreso = 1700100000000L,
                nombres = "Ana Torres",
                sexo = "Femenino",
                sueldo = 42000.0
            )
        )

        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeEmpleados().first()

        assertEquals(2, result.size)
        assertEquals("Carlos Ortega", result[0].nombres)
        assertEquals("Ana Torres", result[1].nombres)
    }

    @Test
    fun `getEmpleado retorna empleado cuando existe`() = runTest {

        val entity = EmpleadoEntity(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Carlos Ortega",
            sexo = "Masculino",
            sueldo = 35000.0
        )

        coEvery { dao.getById(1) } returns entity

        val result = repository.getEmpleado(1)

        assertNotNull(result)
        assertEquals("Carlos Ortega", result?.nombres)
        assertEquals(35000.0, result?.sueldo ?: 0.0, 0.001)
    }

    @Test
    fun `getEmpleado retorna null cuando no existe`() = runTest {

        coEvery { dao.getById(99) } returns null

        val result = repository.getEmpleado(99)

        assertNull(result)
    }

    @Test
    fun `upsert inserta nuevo empleado y retorna id`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = 1700000000000L,
            nombres = "Juan Silver",
            sexo = "Masculino",
            sueldo = 28000.0
        )

        coEvery { dao.upsert(any()) } returns 5L

        val result = repository.upsert(empleado)

        assertEquals(5, result)

        coVerify {
            dao.upsert(any())
        }
    }

    @Test
    fun `upsert actualiza empleado existente y retorna mismo id`() = runTest {

        val empleado = Empleado(
            empleadoId = 3,
            fechaIngreso = 1700000000000L,
            nombres = "Maria Gonzalez",
            sexo = "Femenino",
            sueldo = 45000.0
        )

        coEvery { dao.upsert(any()) } returns 3L

        val result = repository.upsert(empleado)

        assertEquals(3, result)
    }

    @Test
    fun `delete llama deleteById del dao`() = runTest {

        coEvery { dao.deletebyId(1) } just Runs

        repository.delete(1)

        coVerify {
            dao.deletebyId(1)
        }
    }

    @Test
    fun `getEmpleadosByNombre retorna lista filtrada`() = runTest {

        val entities = listOf(
            EmpleadoEntity(
                empleadoId = 1,
                fechaIngreso = 1700000000000L,
                nombres = "Luis Fernandez",
                sexo = "Masculino",
                sueldo = 32000.0
            )
        )

        coEvery {
            dao.getEempleadoByNombre("Luis Fernandez")
        } returns entities

        val result = repository.getEmpleadosByNombre("Luis Fernandez")

        assertEquals(1, result.size)
        assertEquals("Luis Fernandez", result[0].nombres)
    }
}