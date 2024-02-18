package com.ed.turbowash_android.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ed.turbowash_android.R

sealed class Screen(val route: String, val screenTitle:String, @DrawableRes val selectedIcon:Int, @DrawableRes val unselectedIcon:Int) {
    object Landing : Screen("home", "Home", R.drawable.home_filled, R.drawable.home_outline)
    object Schedule : Screen("schedule", "Schedule", R.drawable.calendar_filled, R.drawable.calendar_outline)
    object MyWashers : Screen("team", "My Washers", R.drawable.group_filled, R.drawable.group_outline)
    object Settings : Screen("settings", "Settings", R.drawable.settings_filled, R.drawable.settings_outline)
}

val screensList = listOf(
    Screen.Landing,
    Screen.Schedule,
    Screen.MyWashers,
    Screen.Settings
)