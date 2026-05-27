package edu.ucne.registroocupaciones.presentation.horaextra.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ObserveEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.horaextra.usecase.DeleteHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.horaextra.usecase.ObserveHoraExtraUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoraExtraListViewModel @Inject constructor(
    private val observeHoraExtraUseCase: ObserveHoraExtraUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HoraExtraListState(isLoading = true))
    val state: StateFlow<HoraExtraListState> = _state.asStateFlow()

    init {
        loadEmpleadoNombres()
        onEvent(HoraExtraListEvent.Load)
    }

    private fun loadEmpleadoNombres() {
        viewModelScope.launch {
            observeEmpleadoUseCase().collectLatest { empleados ->
                val nombres = empleados.associate { it.empleadoId to it.nombres }
                _state.update { it.copy(empleadoNombres = nombres) }
            }
        }
    }

    fun onEvent(event: HoraExtraListEvent) {
        when (event) {
            HoraExtraListEvent.Load -> observeHorasExtras()
            is HoraExtraListEvent.Delete -> onDelete(event.id)
            HoraExtraListEvent.CreateNew -> {}
            is HoraExtraListEvent.Edit -> {}
        }
    }

    private fun observeHorasExtras() {
        viewModelScope.launch {
            observeHoraExtraUseCase().collectLatest { horasExtrasList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        horasExtras = horasExtrasList
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteHoraExtraUseCase(id)
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al eliminar: ${e.message}") }
            }
        }
    }
}