package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomContractCard1
import com.ed.turbowash_android.viewmodels.ContractsViewModel
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun CompletedBookingsActivity(
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController
){
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    val contractsViewModel: ContractsViewModel = hiltViewModel()
    val contractsLoading = contractsViewModel.loading.collectAsState()
    val contracts = contractsViewModel.contractsList.collectAsState()
    val contractsError = contractsViewModel.error.collectAsState()

    LaunchedEffect(key1 = contractsViewModel) {
        contractsViewModel.getContractsListUnderProfile()
    }

    Scaffold () { paddingValues ->
        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            when {
                loadingProfile.value -> {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                color = colorResource(id = R.color.turboBlue),
                                strokeWidth = 5.dp
                            )
                        }
                    }
                }

                !error.value.isNullOrBlank() -> {
                    item { Text(text = error.value!!, color = Color.Red) }
                }

                customer.value != null -> {
                    when {
                        contractsLoading.value -> {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CircularProgressIndicator(
                                        color = colorResource(id = R.color.turboBlue),
                                        strokeWidth = 5.dp
                                    )
                                }
                            }
                        }

                        !contractsError.value.isNullOrBlank() -> {
                            item { Text(text = error.value!!, color = Color.Red) }
                        }

                        contracts.value.isEmpty() && !contractsLoading.value && contractsError.value.isNullOrBlank() -> {
                            item {
                                Text(
                                    text = "No completed contracts found under your profile!",
                                    color = Color.Black
                                )
                            }
                        }

                        contracts.value.isNotEmpty() && !contractsLoading.value && contractsError.value.isNullOrBlank() -> {
                            val completedContracts = contracts.value.filter { contract ->
                                contract.contractStatus.briefMessage == "Cancelled" || contract.contractStatus.briefMessage == "Completed"
                            }
                            items(completedContracts, key = { it.id }) { contract ->
                                CustomContractCard1(
                                    contract = contract,
                                    onClickContract = {

                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}