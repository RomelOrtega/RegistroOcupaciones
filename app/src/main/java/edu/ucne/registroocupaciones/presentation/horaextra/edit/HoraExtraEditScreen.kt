package edu.ucne.registroocupaciones.presentation.horaextra.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraEditScreen(
    horaExtraId: Int?,
    viewModel: HoraExtraEditViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(horaExtraId) {
        viewModel.onEvent(HoraExtraEditUIEvent.Load(horaExtraId))
    }

    if (uiState.saved || uiState.deleted) {
        SideEffect { goBack() }
    }

    HoraExtraEditBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        onDrawer = onDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraEditBody(
    uiState: HoraExtraEditUIState,
    onEvent: (HoraExtraEditUIEvent) -> Unit,
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {
    var empleadoExpanded by remember { mutableStateOf(false) }
    var tipoExpanded by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val tiposHoraExtra = listOf("Día", "Nocturna", "Día Feriado/Descanso", "Feriado Nocturno")

    if (uiState.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.fecha
        )
        DatePickerDialog(
            onDismissRequest = { onEvent(HoraExtraEditUIEvent.HideDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onEvent(HoraExtraEditUIEvent.FechaChanged(millis))
                    }
                    onEvent(HoraExtraEditUIEvent.HideDatePicker)
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(HoraExtraEditUIEvent.HideDatePicker) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.isNew) "Nueva Hora Extra" else "Editar Hora Extra") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = empleadoExpanded,
                        onExpandedChange = { empleadoExpanded = !empleadoExpanded }
                    ) {
                        OutlinedTextField(
                            label = { Text("Empleado") },
                            value = uiState.empleadoNombre,
                            onValueChange = {},
                            readOnly = true,
                            isError = uiState.empleadoError != null,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = empleadoExpanded,
                            onDismissRequest = { empleadoExpanded = false }
                        ) {
                            uiState.empleados.forEach { empleado ->
                                DropdownMenuItem(
                                    text = { Text("${empleado.nombres} - Sueldo: ${"%.2f".format(empleado.sueldo)}") },
                                    onClick = {
                                        onEvent(HoraExtraEditUIEvent.EmpleadoChanged(empleado.empleadoId, empleado.nombres))
                                        empleadoExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    uiState.empleadoError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        label = { Text("Fecha") },
                        value = if (uiState.fecha != null) dateFormatter.format(Date(uiState.fecha)) else "",
                        onValueChange = {},
                        readOnly = true,
                        isError = uiState.fechaError != null,
                        trailingIcon = {
                            IconButton(onClick = { onEvent(HoraExtraEditUIEvent.ShowDatePicker) }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEvent(HoraExtraEditUIEvent.ShowDatePicker) }
                    )
                    uiState.fechaError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = tipoExpanded,
                        onExpandedChange = { tipoExpanded = !tipoExpanded }
                    ) {
                        OutlinedTextField(
                            label = { Text("Tipo de Hora Extra") },
                            value = uiState.tipoHoraExtra,
                            onValueChange = {},
                            readOnly = true,
                            isError = uiState.tipoHoraExtraError != null,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar tipo")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = tipoExpanded,
                            onDismissRequest = { tipoExpanded = false }
                        ) {
                            tiposHoraExtra.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo) },
                                    onClick = {
                                        onEvent(HoraExtraEditUIEvent.TipoHoraExtraChanged(tipo))
                                        tipoExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    uiState.tipoHoraExtraError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        label = { Text("Cantidad de Horas (semana)") },
                        value = uiState.cantidadHoras?.toString() ?: "",
                        onValueChange = { onEvent(HoraExtraEditUIEvent.CantidadHorasChanged(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = uiState.cantidadHorasError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.cantidadHorasError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }
                    if (uiState.empleadoId != null && uiState.cantidadHoras != null && uiState.cantidadHoras > 0 && uiState.tipoHoraExtra.isNotBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Desglose del Cálculo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Tipo: ${uiState.tipoHoraExtra}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Sueldo por día: ${"%.2f".format(uiState.sueldoPorDia)}")
                        Text("Sueldo por hora: ${"%.2f".format(uiState.sueldoPorHora)}")
                        Spacer(modifier = Modifier.height(4.dp))

                        if (uiState.horasAl35 > 0) {
                            Text("Horas al 35%: ${"%.1f".format(uiState.horasAl35)}h → ${"%.2f".format(uiState.montoHoras35)}")
                        }
                        if (uiState.horasAl100 > 0) {
                            Text("Horas al 100%: ${"%.1f".format(uiState.horasAl100)}h → ${"%.2f".format(uiState.montoHoras100)}")
                        }
                        if (uiState.montoNocturno > 0) {
                            Text("Recargo nocturno 15%: ${"%.2f".format(uiState.montoNocturno)}")
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "TOTAL A PAGAR: ${"%.2f".format(uiState.montoTotal)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(HoraExtraEditUIEvent.Save) },
                            enabled = !uiState.isSaving
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }

                        if (!uiState.isNew) {
                            OutlinedButton(
                                onClick = { onEvent(HoraExtraEditUIEvent.Delete) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}