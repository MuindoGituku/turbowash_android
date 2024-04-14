/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.detailsscreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.ChatBubbleTileView
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.viewmodels.ContractsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractDetailsChatScreen(
    contractID: String,
    onClickBackArrow: () -> Unit,
) {
    val contractsViewModel: ContractsViewModel = hiltViewModel()
    val contractInView = contractsViewModel.contractInView.collectAsState().value
    val contractLoading = contractsViewModel.loading.collectAsState().value
    val contractError = contractsViewModel.error.collectAsState().value

    val newMessageText = remember { mutableStateOf("") }
    val newMessageTextValidationError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        contractsViewModel.getIndividualContractInfo(contractID)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (contractInView != null) "Chat with ${
                            contractInView.provider.fullNames.split(
                                " "
                            ).first()
                        }" else "Chat Screen",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onClickBackArrow() }) {
                        CustomPaddedIcon(
                            icon = R.drawable.chev_left,
                            backgroundPadding = 5
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Unspecified,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = colorResource(id = R.color.turboBlue),
                    titleContentColor = Color.Black,
                    actionIconContentColor = colorResource(id = R.color.turboBlue),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 0.dp,
                        end = 5.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                when {
                    contractLoading -> {
                        item {
                            FullScreenLottieAnimWithText(
                                lottieResource = R.raw.loading,
                                loadingAnimationText = "Fetching contract details. Please wait...",
                            )
                        }
                    }

                    !contractError.isNullOrBlank() -> {
                        item {
                            ErrorAnimatedScreenWithTryAgain(
                                lottieResource = R.raw.doc_error,
                                errorText = "$contractError. Click the button below to retry.",
                                retryAction = {
                                    contractsViewModel.getIndividualContractInfo(
                                        contractID
                                    )
                                },
                                retryButtonText = "Try Again"
                            )
                        }
                    }

                    contractInView != null -> {
                        if (contractInView.conversation.isEmpty()) {
                            item {
                                FullScreenLottieAnimWithText(
                                    lottieResource = R.raw.empty_list,
                                    loadingAnimationText = "No messages have been sent to the chat yet. To communicate with your washer, enter your message below...",
                                )
                            }
                        } else {
                            items(contractInView.conversation) {
                                ChatBubbleTileView(
                                    chatMessage = it,
                                    senderName = if (it.senderID == contractInView.customer.profileID) "You" else contractInView.provider.fullNames,
                                    senderImage = if (it.senderID == contractInView.customer.profileID) contractInView.customer.imageUrl else contractInView.provider.imageUrl,
                                    customerMessage = it.senderID == contractInView.customer.profileID
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 5.dp,
                        bottom = 10.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CustomIconTextField(
                    fieldLabel = "Type a message...",
                    fieldValue = newMessageText,
                    hasValidationError = newMessageTextValidationError,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    singleLine = false,
                    textInputAutoCap = KeyboardCapitalization.Sentences,
                )
                Box(
                    modifier = Modifier.clickable(
                        enabled = newMessageText.value.trim().isNotEmpty()
                    ) {
                        contractsViewModel.sendChatMessageToContract(
                            contractID = contractID,
                            message = newMessageText.value.trim()
                        ).also {
                            newMessageText.value = ""
                            focusManager.clearFocus()
                        }
                    }) {
                    CustomPaddedIcon(
                        icon = R.drawable.upload,
                        backgroundPadding = 10,
                        backgroundColor = if (newMessageText.value.trim()
                                .isNotEmpty()
                        ) colorResource(
                            id = R.color.turboBlue
                        ) else colorResource(id = R.color.fadedGray),
                        iconColor = if (newMessageText.value.trim().isNotEmpty()) colorResource(
                            id = R.color.fadedGray
                        ) else colorResource(id = R.color.black),
                    )
                }
            }
        }
    }
}