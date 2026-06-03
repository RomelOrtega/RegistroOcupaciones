package edu.ucne.registroocupaciones.presentation.horaextra.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.Empleado.usecase.ObserveEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horaextra.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HoraExtraEditViewModel @Inject constructor(
    private val getHoraExtraUseCase: GetHoraExtraUseCase,
    private val upsertHoraExtraUseCase: UpsertHoraExtraUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase,
    private val validateHoraExtraUseCase: ValidateHoraExtraUseCase,
    private val calcularHoraExtraUseCase: CalcularHoraExtraUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HoraExtraEditUIState())
    val state: StateFlow<HoraExtraEditUIState> = _state.asStateFlow()

    init {
        loadEmpleados()
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            observeEmpleadoUseCase().collectLatest { empleados ->
                _state.update { it.copy(empleados = empleados) }
            }
        }
    }

    private fun recalcular() {
        val empleado = _state.value.empleados.find { it.empleadoId == _state.value.empleadoId }
        val horas = _state.value.cantidadHoras
        val tipo = _state.value.tipoHoraExtra
        if (empleado != null && horas != null && horas > 0 && tipo.isNotBlank()) {
            val resultado = calcularHoraExtraUseCase(
                sueldoMensual = empleado.sueldo,
                cantidadHoras = horas,
                tipoHoraExtra = tipo
            )
            _state.update {
                it.copy(
                    sueldoPorDia = resultado.sueldoPorDia,
                    sueldoPorHora = resultado.sueldoPorHora,
                    horasAl35 = resultado.horasAl35,
                    horasAl100 = resultado.horasAl100,
                    montoHoras35 = resultado.montoHoras35,
                    montoHoras100 = resultado.montoHoras100,
                    montoNocturno = resultado.montoNocturno,
                    montoTotal = resultado.montoTotal
                )
            }
        }
    }

    fun onEvent(event: HoraExtraEditUIEvent) {
        when (event) {
            is HoraExtraEditUIEvent.Load -> onLoad(event.id)
            is HoraExtraEditUIEvent.EmpleadoChanged -> {
                _state.update {
                    it.copy(
                        empleadoId = event.empleadoId,
                        empleadoNombre = event.nombre,
                        empleadoError = null
                    )
                }
                recalcular()
            }
            is HoraExtraEditUIEvent.FechaChanged -> {
                _state.update { it.copy(fecha = event.value, fechaError = null) }
            }
            is HoraExtraEditUIEvent.CantidadHorasChanged -> {
                val horas = event.value.toDoubleOrNull()
                _state.update { it.copy(cantidadHoras = horas, cantidadHorasError = null) }
                recalcular()
            }
            is HoraExtraEditUIEvent.TipoHoraExtraChanged -> {
                _state.update { it.copy(tipoHoraExtra = event.value, tipoHoraExtraError = null) }
                recalcular()
            }
            HoraExtraEditUIEvent.Save -> onSave()
            HoraExtraEditUIEvent.Delete -> onDelete()
            HoraExtraEditUIEvent.ShowDatePicker -> _state.update { it.copy(showDatePicker = true) }
            HoraExtraEditUIEvent.HideDatePicker -> _state.update { it.copy(showDatePicker = false) }
        }
    }

    private fun onLoad(id: Int?) {
        _state.value = HoraExtraEditUIState()
        loadEmpleados()
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, horaExtraId = null) }
            return
        }
        viewModelScope.launch {
            val horaExtra = getHoraExtraUseCase(id)
            horaExtra?.let { item ->
                val empleado = _state.value.empleados.find { it.empleadoId == item.empleadoId }
                _state.update {
                    it.copy(
                        isNew = false,
                        horaExtraId = item.horaExtraId,
                        empleadoId = item.empleadoId,
                        empleadoNombre = empleado?.nombres ?: "",
                        fecha = item.fecha,
                        cantidadHoras = item.cantidadHoras,
                        tipoHoraExtra = if (item.esNocturna) "Nocturna" else "Día",
                        montoTotal = item.montoTotal,
                        horasAl35 = item.horaAl35,
                        horasAl100 = item.horasAl100
                    )
                }
                recalcular()
            }
        }
    }

    private fun onSave() {
        val validation = validateHoraExtraUseCase(
            empleadoId = _state.value.empleadoId,
            fecha = _state.value.fecha,
            cantidadHoras = _state.value.cantidadHoras
        )

        val tipoError = if (_state.value.tipoHoraExtra.isBlank()) "Debe seleccionar el tipo de hora extra" else null

        if (!validation.isValid || tipoError != null) {
            _state.update {
                it.copy(
                    empleadoError = validation.empleadoError,
                    fechaError = validation.fechaError,
                    cantidadHorasError = validation.cantidadHorasError,
                    tipoHoraExtraError = tipoError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val esNocturna = _state.value.tipoHoraExtra == "Nocturna" || _state.value.tipoHoraExtra == "Feriado Nocturno"
                val horaExtra = HoraExtra(
                    horaExtraId = _state.value.horaExtraId ?: 0,
                    empleadoId = _state.value.empleadoId ?: 0,
                    fecha = _state.value.fecha ?: 0L,
                    cantidadHoras = _state.value.cantidadHoras ?: 0.0,
                    esNocturna = esNocturna,
                    horaAl35 = _state.value.horasAl35,
                    horasAl100 = _state.value.horasAl100,
                    montoTotal = _state.value.montoTotal
                )
                upsertHoraExtraUseCase(horaExtra)
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, empleadoError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.horaExtraId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteHoraExtraUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, empleadoError = e.message) }
            }
        }
    }
}