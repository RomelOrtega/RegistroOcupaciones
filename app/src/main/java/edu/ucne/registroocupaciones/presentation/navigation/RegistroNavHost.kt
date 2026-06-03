package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registroocupaciones.presentation.empleado.EmpleadoAdaptiveScreen
import edu.ucne.registroocupaciones.presentation.horaextra.HoraExtraAdaptiveScreen
import edu.ucne.registroocupaciones.presentation.Ocupaciones.OcupacionAdaptiveScreen

enum class NavItem(
    val title: String,
    val icon: ImageVector,
    val startDestination: Screen
) {
    Ocupaciones("Ocupaciones", Icons.Default.Work, Screen.OcupacionList),
    Empleados("Empleados", Icons.Default.People, Screen.EmpleadoList),
    HorasExtras("Horas Extras", Icons.Default.AccessTime, Screen.HoraExtraList)
}

@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {
    var selectedItem by remember { mutableStateOf(NavItem.Ocupaciones) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            NavItem.entries.forEach { navItem ->
                item(
                    selected = selectedItem == navItem,
                    onClick = {
                        selectedItem = navItem
                        navHostController.navigate(navItem.startDestination) {
                            launchSingleTop = true
                            popUpTo(Screen.OcupacionList) { inclusive = false }
                        }
                    },
                    icon = { Icon(navItem.icon, contentDescription = navItem.title) },
                    label = { Text(navItem.title) }
                )
            }
        }
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.OcupacionList
        ) {
            composable<Screen.OcupacionList> {
                OcupacionAdaptiveScreen()
            }

            composable<Screen.EmpleadoList> {
                EmpleadoAdaptiveScreen()
            }

            composable<Screen.HoraExtraList> {
                HoraExtraAdaptiveScreen()
            }
        }
    }
}