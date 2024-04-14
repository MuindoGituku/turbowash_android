/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

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
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.viewmodels.ContractsViewModel
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun CompletedBookingsActivity(
    onClickedContractCard: (String) -> Unit,
) {
    val contractsViewModel: ContractsViewModel = hiltViewModel()
    val contractsLoading = contractsViewModel.loading.collectAsState()
    val contracts = contractsViewModel.contractsList.collectAsState()
    val contractsError = contractsViewModel.error.collectAsState()

    LaunchedEffect(key1 = Unit) {
        contractsViewModel.getContractsListUnderProfile()
    }

    Scaffold() { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            when {
                contractsLoading.value -> {
                    item {
                        FullScreenLottieAnimWithText(
                            lottieResource = R.raw.loading,
                            loadingAnimationText = "Fetching booked contracts under your profile. Please wait...",
                        )
                    }
                }

                !contractsError.value.isNullOrBlank() -> {
                    item {
                        ErrorAnimatedScreenWithTryAgain(
                            lottieResource = R.raw.doc_error,
                            errorText = "$contractsError. Click the button below to retry.",
                            retryAction = { contractsViewModel.getContractsListUnderProfile() },
                            retryButtonText = "Try Again"
                        )
                    }
                }

                contracts.value.isEmpty() -> {
                    item {
                        FullScreenLottieAnimWithText(
                            lottieResource = R.raw.empty_list,
                            loadingAnimationText = "You have no past or completed contracts under your profile. Closed contracts will appear here.",
                        )
                    }
                }

                contracts.value.isNotEmpty() -> {
                    val completedContracts = contracts.value.filter { contract ->
                        contract.contractStatus.briefMessage != "Proposed" && contract.contractStatus.briefMessage != "Confirmed"
                    }

                    if (completedContracts.isEmpty()) {
                        item {
                            FullScreenLottieAnimWithText(
                                lottieResource = R.raw.empty_list,
                                loadingAnimationText = "You have no past or completed contracts under your profile. Closed contracts will appear here.",
                            )
                        }
                    } else {
                        items(completedContracts, key = { it.id }) { contract ->
                            CustomContractCard1(
                                contract = contract,
                                onClickContract = {
                                    onClickedContractCard(contract.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}