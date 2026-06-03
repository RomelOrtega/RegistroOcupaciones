package edu.ucne.registroocupaciones.presentation.empleado.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import edu.ucne.registroocupaciones.domain.Empleado.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ValidateEmpleadoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoEditViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    private val validateEmpleadoUseCase: ValidateEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoEditUIState())
    val state: StateFlow<EmpleadoEditUIState> = _state.asStateFlow()

    fun onEvent(event: EmpleadoEditUIEvent) {
        when (event) {
            is EmpleadoEditUIEvent.Load -> onLoad(event.id)
            is EmpleadoEditUIEvent.FechaIngresoChanged -> {
                _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
            }
            is EmpleadoEditUIEvent.NombresChanged -> {
                _state.update { it.copy(nombres = event.value, nombresError = null) }
            }
            is EmpleadoEditUIEvent.SexoChanged -> {
                _state.update { it.copy(sexo = event.value, sexoError = null) }
            }
            is EmpleadoEditUIEvent.SueldoChanged -> {
                val sueldoDouble = event.value.toDoubleOrNull()
                _state.update { it.copy(sueldo = sueldoDouble, sueldoError = null) }
            }
            EmpleadoEditUIEvent.Save -> onSave()
            EmpleadoEditUIEvent.Delete -> onDelete()
            EmpleadoEditUIEvent.ShowDatePicker -> {
                _state.update { it.copy(showDatePicker = true) }
            }
            EmpleadoEditUIEvent.HideDatePicker -> {
                _state.update { it.copy(showDatePicker = false) }
            }
        }
    }

    private fun onLoad(id: Int?) {
        _state.value = EmpleadoEditUIState()
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }
        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            empleado?.let { item ->
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = item.empleadoId,
                        fechaIngreso = item.fechaIngreso,
                        nombres = item.nombres,
                        sexo = item.sexo,
                        sueldo = item.sueldo
                    )
                }
            }
        }
    }

    private fun onSave() {
        val validation = validateEmpleadoUseCase(
            fechaIngreso = _state.value.fechaIngreso ?: 0L,
            nombres = _state.value.nombres,
            sexo = _state.value.sexo,
            sueldo = _state.value.sueldo
        )

        if (!validation.isValid) {
            _state.update {
                it.copy(
                    fechaIngresoError = validation.fechaIngresoError,
                    nombresError = validation.nombresError,
                    sexoError = validation.sexoError,
                    sueldoError = validation.sueldoError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val empleado = Empleado(
                    empleadoId = _state.value.empleadoId ?: 0,
                    fechaIngreso = _state.value.fechaIngreso ?: 0L,
                    nombres = _state.value.nombres,
                    sexo = _state.value.sexo,
                    sueldo = _state.value.sueldo ?: 0.0
                )
                upsertEmpleadoUseCase(empleado)
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, nombresError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteEmpleadoUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, nombresError = e.message) }
            }
        }
    }
}