package edu.ucne.registroocupaciones.presentation.empleado.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoEditScreen(
    empleadoId: Int?,
    viewModel: EmpleadoEditViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(empleadoId) {
        viewModel.onEvent(EmpleadoEditUIEvent.Load(empleadoId))
    }

    if (uiState.saved || uiState.deleted) {
        SideEffect {
            goBack()
        }
    }

    EmpleadoEditBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        onDrawer = onDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoEditBody(
    uiState: EmpleadoEditUIState,
    onEvent: (EmpleadoEditUIEvent) -> Unit,
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {
    val sexoOptions = listOf("Masculino", "Femenino")
    var sexoExpanded by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // DatePicker
    if (uiState.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.fechaIngreso
        )
        DatePickerDialog(
            onDismissRequest = { onEvent(EmpleadoEditUIEvent.HideDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onEvent(EmpleadoEditUIEvent.FechaIngresoChanged(millis))
                    }
                    onEvent(EmpleadoEditUIEvent.HideDatePicker)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(EmpleadoEditUIEvent.HideDatePicker) }) {
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
                title = { Text(if (uiState.isNew) "Nuevo Empleado" else "Editar Empleado") },
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
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                    // Fecha de ingreso
                    OutlinedTextField(
                        label = { Text("Fecha de Ingreso") },
                        value = if (uiState.fechaIngreso != null) {
                            dateFormatter.format(Date(uiState.fechaIngreso))
                        } else "",
                        onValueChange = {},
                        readOnly = true,
                        isError = uiState.fechaIngresoError != null,
                        trailingIcon = {
                            IconButton(onClick = { onEvent(EmpleadoEditUIEvent.ShowDatePicker) }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEvent(EmpleadoEditUIEvent.ShowDatePicker) }
                    )
                    uiState.fechaIngresoError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nombres
                    OutlinedTextField(
                        label = { Text("Nombres") },
                        value = uiState.nombres,
                        onValueChange = { onEvent(EmpleadoEditUIEvent.NombresChanged(it)) },
                        isError = uiState.nombresError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.nombresError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sexo como DropDown Select
                    ExposedDropdownMenuBox(
                        expanded = sexoExpanded,
                        onExpandedChange = { sexoExpanded = !sexoExpanded }
                    ) {
                        OutlinedTextField(
                            label = { Text("Sexo") },
                            value = uiState.sexo,
                            onValueChange = {},
                            readOnly = true,
                            isError = uiState.sexoError != null,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar sexo")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = sexoExpanded,
                            onDismissRequest = { sexoExpanded = false }
                        ) {
                            sexoOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        onEvent(EmpleadoEditUIEvent.SexoChanged(option))
                                        sexoExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    uiState.sexoError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sueldo
                    OutlinedTextField(
                        label = { Text("Sueldo") },
                        value = uiState.sueldo?.toString() ?: "",
                        onValueChange = { onEvent(EmpleadoEditUIEvent.SueldoChanged(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = uiState.sueldoError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.sueldoError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(EmpleadoEditUIEvent.Save) },
                            enabled = !uiState.isSaving
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }

                        if (!uiState.isNew) {
                            OutlinedButton(
                                onClick = { onEvent(EmpleadoEditUIEvent.Delete) },
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