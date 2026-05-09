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
        }
    }
}