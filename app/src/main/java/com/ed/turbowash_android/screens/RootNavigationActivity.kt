package com.ed.turbowash_android.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.ed.turbowash_android.models.screensList
import com.ed.turbowash_android.models.Screen

class RootNavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurboWash_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootHomeNavigation()
                }
            }
        }
    }
}

@Composable
fun RootHomeNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Landing.route, Modifier.padding(innerPadding)) {
            composable(Screen.Landing.route) { HomeScreen() }
            composable(Screen.Schedule.route) { DashboardScreen() }
            composable(Screen.MyWashers.route) { NotificationsScreen() }
            composable(Screen.Settings.route) { ProfileScreen() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RootHomeNavigationPreview() {
    TurboWash_AndroidTheme {
        RootHomeNavigation()
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        screensList.forEach { screen ->
            BottomNavigationItem(
                icon = { Image(painterResource(id = if (currentRoute == screen.route) screen.selectedIcon else screen.unselectedIcon), contentDescription = null) },
                label = { Text(screen.screenTitle) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun HomeScreen() { /* Content for Home Screen */ }

@Composable
fun DashboardScreen() { /* Content for Dashboard Screen */ }

@Composable
fun NotificationsScreen() { /* Content for Notifications Screen */ }

@Composable
fun ProfileScreen() { /* Content for Profile Screen */ }