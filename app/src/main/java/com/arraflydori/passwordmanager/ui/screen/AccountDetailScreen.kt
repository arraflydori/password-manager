package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.ui.composable.MyTextField
import com.arraflydori.passwordmanager.viewmodel.AccountDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    viewModel: AccountDetailViewModel,
    onSaveSuccess: (Account) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess == true) onSaveSuccess(uiState.account)
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