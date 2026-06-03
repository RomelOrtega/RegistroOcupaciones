package edu.ucne.registroocupaciones.presentation.empleado.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.Empleado.model.Empleado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoListScreen(
    goToEmpleado: (Int) -> Unit,
    createEmpleado: () -> Unit,
    viewModel: EmpleadoListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Listado de Empleados") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { createEmpleado() }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(state.empleados) { empleado ->
                    EmpleadoCard(
                        empleado = empleado,
                        onClick = { goToEmpleado(empleado.empleadoId) },
                        onDelete = { viewModel.onEvent(EmpleadoListEvent.Delete(empleado.empleadoId)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmpleadoCard(
    empleado: Empleado,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(empleado.nombres, style = MaterialTheme.typography.titleMedium)
                Text("Fecha: ${dateFormatter.format(Date(empleado.fechaIngreso))}", style = MaterialTheme.typography.bodyMedium)
                Text("Sexo: ${empleado.sexo} | Sueldo: ${empleado.sueldo}", style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = { onClick() }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}