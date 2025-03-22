package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.ui.composable.MyTextField
import com.arraflydori.passwordmanager.viewmodel.AccountDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AccountDetailScreen(
    viewModel: AccountDetailViewModel,
    onSaveSuccess: (Account) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val availableTags = uiState.tagOptions.filter { !uiState.account.tags.contains(it) }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess == true) onSaveSuccess(uiState.account)
    }

    if (uiState.showTagOptions) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = {
                viewModel.toggleTagOptionsVisibility()
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 12.dp, 8.dp, 16.dp)
                    .animateContentSize()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Select tags", style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(
                        onClick = {
                            viewModel.toggleTagOptionsVisibility()
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close tag options")
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    for (tag in availableTags) {
                        FilterChip(
                            selected = false,
                            onClick = {
                                if (availableTags.size == 1) viewModel.toggleTagOptionsVisibility()
                                viewModel.addTag(tag)
                            },
                            label = {
                                Text(tag)
                            }
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Account")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Go back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.apply {
                MyTextField(
                    value = account.platformName,
                    onValueChange = {
                        viewModel.update(platformName = it)
                    },
                    hint = "Platform",
                    modifier = Modifier.fillMaxWidth(),
                )
                MyTextField(
                    value = account.username ?: "",
                    onValueChange = {
                        viewModel.update(username = it)
                    },
                    hint = "Username",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                MyTextField(
                    value = account.email ?: "",
                    onValueChange = {
                        viewModel.update(email = it)
                    },
                    hint = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                MyTextField(
                    value = account.password,
                    onValueChange = {
                        viewModel.update(password = it)
                    },
                    hint = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailing = {
                        IconButton(
                            onClick = {
                                viewModel.togglePasswordVisibility()
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tags", style = MaterialTheme.typography.titleSmall)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val canRemove = uiState.account.tags.size > 1
                    for (tag in uiState.account.tags) {
                        FilterChip(
                            selected = true,
                            onClick = {
                                if (canRemove) viewModel.removeTag(tag)
                            },
                            trailingIcon = if (canRemove) {
                                {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove tag",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            label = {
                                Text(tag)
                            }
                        )
                    }
                    if (!availableTags.isEmpty()) FilledTonalIconButton(
                        onClick = {
                            viewModel.toggleTagOptionsVisibility()
                        },
                        shape = RoundedCornerShape(percent = 24),
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add tag",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.save()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    enabled = account.platformName.isNotBlank() && account.password.isNotBlank()
                ) {
                    Text("Save")
                }
            }
        }
    }
}