package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.CredentialType
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
                                Text(tag.label)
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
                .consumeWindowInsets(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.apply {
                MyTextField(
                    value = account.platformName,
                    onValueChange = {
                        viewModel.update(platformName = it)
                    },
                    hint = "Platform",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                    trailing = {
                        if (account.platformName.isBlank()) {
                            Icon(
                                Icons.Default.PriorityHigh,
                                contentDescription = "Empty credential",
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                )
                MyTextField(
                    value = account.username ?: "",
                    onValueChange = {
                        viewModel.update(username = it)
                    },
                    hint = "Username",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                )
                MyTextField(
                    value = account.email ?: "",
                    onValueChange = {
                        viewModel.update(email = it)
                    },
                    hint = "Email",
                    trailing = {
                        if (error.invalidEmail) {
                            Icon(
                                Icons.Default.PriorityHigh,
                                contentDescription = "Invalid email",
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                MyTextField(
                    value = account.note,
                    onValueChange = {
                        viewModel.update(note = it)
                    },
                    hint = "Note",
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (account.credentials.isEmpty()) ImeAction.Done else ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Credentials", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                viewModel.createCredential()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add credential",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    if (account.credentials.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    for ((i, cred) in account.credentials.withIndex()) {
                        var showPassword by remember { mutableStateOf(false) }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // TODO: Add Type selector
                            MyTextField(
                                value = cred.value,
                                onValueChange = {
                                    viewModel.updateCredential(id = cred.id, value = it)
                                },
                                hint = when (cred.type) {
                                    CredentialType.PIN -> "PIN"
                                    CredentialType.Password -> "Password"
                                },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    // TODO: Fix this
                                    imeAction = if (i != account.credentials.size - 1) ImeAction.Done else ImeAction.Next
                                ),
                                trailing = {
                                    if (cred.value.isBlank()) {
                                        Icon(
                                            Icons.Default.PriorityHigh,
                                            contentDescription = "Empty credential",
                                            tint = MaterialTheme.colorScheme.onError,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = CircleShape
                                                )
                                                .padding(4.dp)
                                        )
                                    } else {
                                        IconButton(
                                            onClick = {
                                                showPassword = !showPassword
                                            },
                                            modifier = Modifier.size(16.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                                contentDescription = if (showPassword) "Hide password" else "Show password"
                                            )
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    viewModel.removeCredential(cred)
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove credential",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (i != account.credentials.size - 1) Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text("Tags", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.width(8.dp))
                        if (!availableTags.isEmpty()) IconButton(
                            onClick = {
                                viewModel.toggleTagOptionsVisibility()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add tag",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
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
                                    Text(tag.label)
                                }
                            )
                        }
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
                    enabled = canSave
                ) {
                    Text("Save")
                }
            }
        }
    }
}