package edu.ucne.registroocupaciones.presentation.empleado

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.ucne.registroocupaciones.presentation.empleado.edit.EmpleadoEditScreen
import edu.ucne.registroocupaciones.presentation.empleado.list.EmpleadoListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EmpleadoAdaptiveScreen() {
    var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch { navigator.navigateBack() }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                EmpleadoListScreen(
                    goToEmpleado = { id ->
                        selectedId = id
                        scope.launch { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail) }
                    },
                    createEmpleado = {
                        selectedId = 0
                        scope.launch { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail) }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                if (selectedId != null) {
                    EmpleadoEditScreen(
                        empleadoId = selectedId,
                        goBack = {
                            selectedId = null
                            scope.launch { navigator.navigateBack() }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Seleccione un empleado")
                    }
                }
            }
        }
    )
}