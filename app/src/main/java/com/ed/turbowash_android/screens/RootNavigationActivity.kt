package com.ed.turbowash_android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.ed.turbowash_android.models.screensList
import com.ed.turbowash_android.models.Screen

@Composable
fun RootHomeNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Landing.route,
            Modifier.padding(innerPadding)
        ) {
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
    BottomNavigation(
        backgroundColor = Color.White,
    ) {
        val currentRoute = currentRoute(navController)
        screensList.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.padding(vertical = 5.dp),
                icon = {
                    Image(
                        painterResource(
                            id = if (currentRoute == screen.route) screen.selectedIcon else screen.unselectedIcon
                        ),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            if (currentRoute == screen.route) colorResource(
                                id = R.color.turboBlue
                            ) else Color.Gray
                        ),
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                },
                label = {
                    Text(
                        text = screen.screenTitle,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.W700,
                            color = if (currentRoute == screen.route) colorResource(id = R.color.turboBlue) else Color.Gray
                        )
                    )
                },
                alwaysShowLabel = currentRoute == screen.route,
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
fun HomeScreen() { /* Content for Home Screen */
}

@Composable
fun DashboardScreen() { /* Content for Dashboard Screen */
}

@Composable
fun NotificationsScreen() { /* Content for Notifications Screen */
}

@Composable
fun ProfileScreen() { /* Content for Profile Screen */
}