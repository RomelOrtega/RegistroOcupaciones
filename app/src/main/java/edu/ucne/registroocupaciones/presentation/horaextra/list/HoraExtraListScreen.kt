package edu.ucne.registroocupaciones.presentation.horaextra.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.horaextra.model.HoraExtra
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraListScreen(
    onDrawer: () -> Unit,
    goToHoraExtra: (Int) -> Unit,
    createHoraExtra: () -> Unit,
    viewModel: HoraExtraListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HoraExtraListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            when (event) {
                is HoraExtraListEvent.Edit -> goToHoraExtra(event.id)
                HoraExtraListEvent.CreateNew -> createHoraExtra()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoraExtraListBody(
    state: HoraExtraListState,
    onDrawer: () -> Unit,
    onEvent: (HoraExtraListEvent) -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Listado de Horas Extras") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(HoraExtraListEvent.CreateNew) }) {
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
                items(state.horasExtras) { horaExtra ->
                    HoraExtraCard(
                        horaExtra = horaExtra,
                        empleadoNombre = state.empleadoNombres[horaExtra.empleadoId] ?: "Desconocido",
                        dateFormatter = dateFormatter,
                        onClick = { onEvent(HoraExtraListEvent.Edit(horaExtra.horaExtraId)) },
                        onDelete = { onEvent(HoraExtraListEvent.Delete(horaExtra.horaExtraId)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HoraExtraCard(
    horaExtra: HoraExtra,
    empleadoNombre: String,
    dateFormatter: SimpleDateFormat,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
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
                Text(empleadoNombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Fecha: ${dateFormatter.format(Date(horaExtra.fecha))} | Horas: ${horaExtra.cantidadHoras}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "35%: ${"%.1f".format(horaExtra.horaAl35)}h | 100%: ${"%.1f".format(horaExtra.horasAl100)}h${if (horaExtra.esNocturna) " | Nocturna" else ""}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Total: ${"%.2f".format(horaExtra.montoTotal)}",
                    style = MaterialTheme.typography.bodyMedium
                )
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