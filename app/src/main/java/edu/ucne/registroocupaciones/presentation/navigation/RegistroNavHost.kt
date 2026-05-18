package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroocupaciones.presentation.Ocupaciones.edit.OcupacionEditScreen
import edu.ucne.registroocupaciones.presentation.Ocupaciones.list.OcupacionListScreen
import kotlinx.coroutines.launch
import edu.ucne.registroocupaciones.presentation.empleado.edit.EmpleadoEditScreen
import edu.ucne.registroocupaciones.presentation.empleado.list.EmpleadoListScreen


@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.OcupacionList
        ) { composable<Screen.OcupacionList> {
                OcupacionListScreen(
                    onDrawer = { scope.launch { drawerState.open() } },
                    goToOcupacion = { id -> navHostController.navigate(Screen.Ocupacion(id)) },
                    createOcupacion = { navHostController.navigate(Screen.Ocupacion(0)) }
                )
            }

            composable<Screen.Ocupacion> {
                val args = it.toRoute<Screen.Ocupacion>()
                OcupacionEditScreen(
                    ocupacionId = args.ocupacionId,
                    goBack = { navHostController.navigateUp() },
                    onDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable<Screen.EmpleadoList> {
                EmpleadoListScreen(
                    onDrawer = { scope.launch { drawerState.open() } },
                    goToEmpleado = { id -> navHostController.navigate(Screen.Empleado(id)) },
                    createEmpleado = { navHostController.navigate(Screen.Empleado(0)) }
                )
            }

            composable<Screen.Empleado> {
                val args = it.toRoute<Screen.Empleado>()
                EmpleadoEditScreen(
                    empleadoId = args.empleadoId,
                    goBack = { navHostController.navigateUp() },
                    onDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}