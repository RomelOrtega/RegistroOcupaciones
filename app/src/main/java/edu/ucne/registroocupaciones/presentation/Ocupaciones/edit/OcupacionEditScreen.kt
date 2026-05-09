package edu.ucne.registroocupaciones.presentation.Ocupaciones.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionEditScreen(
    ocupacionId: Int?,
    viewModel: OcupacionEditViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ocupacionId) {
        viewModel.onEvent(OcupacionEditUIEvent.Load(ocupacionId))
    }

    if (uiState.saved || uiState.deleted) {
        SideEffect {
            goBack()
        }
    }

    OcupacionEditBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        onDrawer = onDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionEditBody(
    uiState: OcupacionEditUIState,
    onEvent: (OcupacionEditUIEvent) -> Unit,
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.isNew) "Nueva Ocupación" else "Editar Ocupación") },
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
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    OutlinedTextField(
                        label = { Text("Descripción") },
                        value = uiState.descripcion,
                        onValueChange = { onEvent(OcupacionEditUIEvent.DescripcionChanged(it)) },
                        isError = uiState.descripcionError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.descripcionError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        label = { Text("Sueldo") },
                        value = uiState.sueldo?.toString() ?: "",
                        onValueChange = { onEvent(OcupacionEditUIEvent.SueldoChanged(it)) },
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
                            onClick = { onEvent(OcupacionEditUIEvent.Save) },
                            enabled = !uiState.isSaving
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }

                        if (!uiState.isNew) {
                            OutlinedButton(
                                onClick = { onEvent(OcupacionEditUIEvent.Delete) },
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

@Preview(showBackground = true)
@Composable
fun OcupacionEditPreview() {
    MaterialTheme {
       OcupacionEditBody(
           uiState = OcupacionEditUIState(descripcion = "Ingeniero", sueldo = 50000.0),
           onEvent = {},
           goBack = {},
           onDrawer = {}
       )
    }
}