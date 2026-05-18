package edu.ucne.registroocupaciones.presentation.empleado.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.Empleado.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ObserveEmpleadoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoListState(isLoading = true))
    val state: StateFlow<EmpleadoListState> = _state.asStateFlow()

    init {
        onEvent(EmpleadoListEvent.Load)
    }

    fun onEvent(event: EmpleadoListEvent) {
        when (event) {
            EmpleadoListEvent.Load -> observeEmpleados()
            is EmpleadoListEvent.Delete -> onDelete(event.id)
            EmpleadoListEvent.CreateNew -> {}
            is EmpleadoListEvent.Edit -> {}
        }
    }

    private fun observeEmpleados() {
        viewModelScope.launch {
            observeEmpleadoUseCase().collectLatest { empleadosList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        empleados = empleadosList
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteEmpleadoUseCase(id)
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al eliminar: ${e.message}") }
            }
        }
    }
}