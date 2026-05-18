package edu.ucne.registroocupaciones.presentation.edit

import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.usecase.*
import edu.ucne.registroocupaciones.presentation.empleado.edit.EmpleadoEditUIEvent
import edu.ucne.registroocupaciones.presentation.empleado.edit.EmpleadoEditViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EmpleadoEditViewModelTest {

    private lateinit var viewModel: EmpleadoEditViewModel
    private lateinit var getEmpleado: GetEmpleadoUseCase
    private lateinit var upsertEmpleado: UpsertEmpleadoUseCase
    private lateinit var deleteEmpleado: DeleteEmpleadoUseCase
    private lateinit var validate: ValidateEmpleadoUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getEmpleado = mockk()
        upsertEmpleado = mockk()
        deleteEmpleado = mockk()
        validate = mockk()
        viewModel = EmpleadoEditViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, validate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load con id null inicializa estado nuevo`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.Load(null))
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isNew)
        assertNull(viewModel.state.value.empleadoId)
    }

    @Test
    fun `load con id 0 inicializa estado nuevo`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.Load(0))
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isNew)
    }

    @Test
    fun `load con id existente carga empleado correctamente`() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Miguel Herrera",
            sexo = "Masculino",
            sueldo = 33000.0
        )
        coEvery { getEmpleado(1) } returns empleado

        viewModel.onEvent(EmpleadoEditUIEvent.Load(1))
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isNew)
        assertEquals(1, viewModel.state.value.empleadoId)
        assertEquals("Miguel Herrera", viewModel.state.value.nombres)
        assertEquals("Masculino", viewModel.state.value.sexo)
        assertEquals(33000.0, viewModel.state.value.sueldo)
    }

    @Test
    fun `NombresChanged actualiza nombres y limpia error`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.NombresChanged("Lucia Valdez"))

        assertEquals("Lucia Valdez", viewModel.state.value.nombres)
        assertNull(viewModel.state.value.nombresError)
    }

    @Test
    fun `FechaIngresoChanged actualiza fecha y limpia error`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.FechaIngresoChanged(1700000000000L))

        assertEquals(1700000000000L, viewModel.state.value.fechaIngreso)
        assertNull(viewModel.state.value.fechaIngresoError)
    }

    @Test
    fun `SexoChanged actualiza sexo y limpia error`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.SexoChanged("Femenino"))

        assertEquals("Femenino", viewModel.state.value.sexo)
        assertNull(viewModel.state.value.sexoError)
    }

    @Test
    fun `SueldoChanged actualiza sueldo correctamente`() = runTest {
        viewModel.onEvent(EmpleadoEditUIEvent.SueldoChanged("27500.0"))

        assertEquals(27500.0, viewModel.state.value.sueldo)
        assertNull(viewModel.state.value.sueldoError)
    }

    @Test
    fun `save guarda empleado correctamente cuando datos son validos`() = runTest {
        val validationResult = ValidateEmpleadoUseCase.ValidationResult(isValid = true)
        every { validate(any(), any(), any(), any()) } returns validationResult
        coEvery { upsertEmpleado(any()) } returns Result.success(1)

        viewModel.onEvent(EmpleadoEditUIEvent.NombresChanged("Andres Castillo"))
        viewModel.onEvent(EmpleadoEditUIEvent.FechaIngresoChanged(1700000000000L))
        viewModel.onEvent(EmpleadoEditUIEvent.SexoChanged("Masculino"))
        viewModel.onEvent(EmpleadoEditUIEvent.SueldoChanged("29000.0"))

        viewModel.onEvent(EmpleadoEditUIEvent.Save)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.saved)
        assertFalse(viewModel.state.value.isSaving)
    }

    @Test
    fun `save muestra errores cuando validacion falla`() = runTest {
        val validationResult = ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            nombresError = "El nombre es requerido",
            fechaIngresoError = "La fecha de ingreso es requerida",
            sexoError = "El sexo es requerido",
            sueldoError = "El sueldo es requerido y debe ser mayor a 0"
        )
        every { validate(any(), any(), any(), any()) } returns validationResult

        viewModel.onEvent(EmpleadoEditUIEvent.Save)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.saved)
        assertEquals("El nombre es requerido", viewModel.state.value.nombresError)
        assertEquals("La fecha de ingreso es requerida", viewModel.state.value.fechaIngresoError)
        assertEquals("El sexo es requerido", viewModel.state.value.sexoError)
        assertEquals("El sueldo es requerido y debe ser mayor a 0", viewModel.state.value.sueldoError)
    }

    @Test
    fun `delete elimina empleado correctamente`() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Elena Rios",
            sexo = "Femenino",
            sueldo = 36000.0
        )
        coEvery { getEmpleado(1) } returns empleado
        coEvery { deleteEmpleado(1) } just Runs

        viewModel.onEvent(EmpleadoEditUIEvent.Load(1))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(EmpleadoEditUIEvent.Delete)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.deleted)
        assertFalse(viewModel.state.value.isDeleting)
        coVerify { deleteEmpleado(1) }
    }

    @Test
    fun `delete muestra error cuando falla`() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = 1700000000000L,
            nombres = "Elena Rios",
            sexo = "Femenino",
            sueldo = 36000.0
        )
        coEvery { getEmpleado(1) } returns empleado
        coEvery { deleteEmpleado(1) } throws Exception("Error al eliminar")

        viewModel.onEvent(EmpleadoEditUIEvent.Load(1))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(EmpleadoEditUIEvent.Delete)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.deleted)
        assertEquals("Error al eliminar", viewModel.state.value.nombresError)
    }
}