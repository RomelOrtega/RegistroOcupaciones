package edu.ucne.registroocupaciones.presentation.Ocupaciones.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.Ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registroocupaciones.domain.Ocupaciones.usecase.ObserveOcupacionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcupacionListViewModel @Inject constructor(
    private val observeOcupacionUseCase: ObserveOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OcupacionListState(isLoading = true))
    val state: StateFlow<OcupacionListState> = _state.asStateFlow()

    init {
        onEvent(OcupacionListEvent.Load)
    }
    fun onEvent(event: OcupacionListEvent) {
        when (event) {
            OcupacionListEvent.Load -> observeOcupaciones()
            is OcupacionListEvent.Delete -> onDelete(event.id)
            OcupacionListEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is OcupacionListEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is OcupacionListEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }
    private fun observeOcupaciones() {
        viewModelScope.launch {
            observeOcupacionUseCase().collectLatest { ocupacionesList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        ocupaciones = ocupacionesList,
                        message = null
                    )
                }
            }
        }
    }
    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteOcupacionUseCase(id)
                onEvent(OcupacionListEvent.ShowMessage("Ocupación eliminada"))
            } catch (e: Exception) {
                onEvent(OcupacionListEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }
    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}