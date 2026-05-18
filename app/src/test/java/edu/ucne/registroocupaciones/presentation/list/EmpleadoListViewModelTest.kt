package edu.ucne.registroocupaciones.presentation.list

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ObserveEmpleadoUseCase
import edu.ucne.registroocupaciones.presentation.empleado.list.EmpleadoListEvent
import edu.ucne.registroocupaciones.presentation.empleado.list.EmpleadoListViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EmpleadoListViewModelTest {

    private lateinit var viewModel: EmpleadoListViewModel
    private lateinit var observeEmpleado: ObserveEmpleadoUseCase
    private lateinit var deleteEmpleado: DeleteEmpleadoUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val empleadosList = listOf(
        Empleado(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Fernando Reyes",
            sexo = "Masculino",
            sueldo = 31000.0
        ),
        Empleado(
            empleadoId = 2,
            fechaIngreso = 1700100000000L,
            nombres = "Patricia Navarro",
            sexo = "Femenino",
            sueldo = 37000.0
        ),
        Empleado(
            empleadoId = 3,
            fechaIngreso = 1700200000000L,
            nombres = "Diego Vargas",
            sexo = "Masculino",
            sueldo = 26000.0
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        observeEmpleado = mockk()
        deleteEmpleado = mockk()
        every { observeEmpleado() } returns flowOf(empleadosList)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): EmpleadoListViewModel {
        return EmpleadoListViewModel(observeEmpleado, deleteEmpleado)
    }

    @Test
    fun `init carga lista de empleados`() = runTest {
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(3, viewModel.state.value.empleados.size)
    }

    @Test
    fun `lista contiene empleados correctos`() = runTest {
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Fernando Reyes", viewModel.state.value.empleados[0].nombres)
        assertEquals("Patricia Navarro", viewModel.state.value.empleados[1].nombres)
        assertEquals("Diego Vargas", viewModel.state.value.empleados[2].nombres)
    }

    @Test
    fun `delete elimina empleado correctamente`() = runTest {
        coEvery { deleteEmpleado(1) } just Runs
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(EmpleadoListEvent.Delete(1))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { deleteEmpleado(1) }
    }

    @Test
    fun `delete muestra mensaje de error cuando falla`() = runTest {
        coEvery { deleteEmpleado(1) } throws Exception("Error de BD")
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(EmpleadoListEvent.Delete(1))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error al eliminar: Error de BD", viewModel.state.value.message)
    }

    @Test
    fun `lista vacia cuando no hay empleados`() = runTest {
        every { observeEmpleado() } returns flowOf(emptyList())

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.empleados.isEmpty())
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `isLoading es true al inicio`() = runTest {
        viewModel = createViewModel()

        assertTrue(viewModel.state.value.isLoading)
    }
}