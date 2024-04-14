/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ed.turbowash_android.R

sealed class Screen(val route: String, val screenTitle:String, @DrawableRes val selectedIcon:Int = 0, @DrawableRes val unselectedIcon:Int = 0) {
    data object Landing : Screen("home", "Home", R.drawable.home_filled, R.drawable.home_outline)
    data object Activity : Screen("schedule", "Schedule", R.drawable.calendar_filled, R.drawable.calendar_outline)
    data object Favorites : Screen("team", "My Team", R.drawable.group_filled, R.drawable.group_outline)
    data object Settings : Screen("settings", "Settings", R.drawable.settings_filled, R.drawable.settings_outline)

    data object SavedAddressesList : Screen("addresses_list", "Saved Addresses")
    data object AddSavedAddress : Screen("add_address", "Add Address")
    data object UpdateSelectedAddress : Screen("update_address/{addressTag}", "Update Address") {
        fun createRoute(address: SavedAddress) = "update_address/${address.address}"
    }
    data object SavedVehiclesList : Screen("vehicles_list", "Saved Vehicles")
    data object AddSavedVehicle : Screen("add_vehicle", "Add Vehicle")
    data object UpdateSelectedVehicle : Screen("update_vehicle/{vehicleTag}", "Update Vehicle") {
        fun createRoute(vehicle: SavedVehicle) = "update_vehicle/${vehicle.tag}"
    }
    data object PaymentCardsList : Screen("cards_list", "Payment Cards")
    data object AddPaymentCard : Screen("add_card", "Add Card")
    data object UpdateSelectedCard : Screen("update_card/{cardTag}", "Update Card") {
        fun createRoute(card: PaymentCard) = "update_card/${card.tag}"
    }
    data object ViewCustomerProfile : Screen("view_profile", "View Profile")
    data object UpdateCustomerProfile : Screen("update_profile", "Update Profile")
    data object BookingDetailsConfirmation : Screen("booking_steps","Confirm Booking Details")
    data object BookingProvidersListBrowse : Screen("available_providers", "Browse Available Providers")

    data object ViewContractDetails : Screen("contracts/{contractID}/details", "Contract Details") {
        fun createRoute(contract: String) = "contracts/${contract}/details"
    }

    data object ViewContractChatMessages : Screen("contracts/{contractID}/chat", "Contract Chats") {
        fun createRoute(contract: String) = "contracts/${contract}/chat"
    }

    data object ViewContractMapNavigation : Screen("contracts/{contractID}/map", "Contract Maps") {
        fun createRoute(contract: String) = "contracts/${contract}/map"
    }

    data object ViewProviderDetails : Screen("providers/{providerID}", "Provider Details") {
        fun createRoute(provider: String) = "providers/${provider}/details"
    }
}

val screensList = listOf(
    Screen.Landing,
    Screen.Activity,
    Screen.Favorites,
    Screen.Settings
)