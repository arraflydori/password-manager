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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.ui.composable.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    account: Account?,
    onSave: (Account) -> Unit,
    onBack: () -> Unit,
) {
    var platformName by remember { mutableStateOf(account?.platformName ?: "") }
    var username by remember { mutableStateOf(account?.username ?: "") }
    var email by remember { mutableStateOf(account?.email ?: "") }
    var password by remember { mutableStateOf(account?.password ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            MyTextField(
                value = platformName,
                onValueChange = { platformName = it },
                hint = "Platform",
                modifier = Modifier.fillMaxWidth(),
            )
            MyTextField(
                value = username,
                onValueChange = { username = it },
                hint = "Username",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            MyTextField(
                value = email,
                onValueChange = { email = it },
                hint = "Email",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            MyTextField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailing = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onSave(
                        Account(
                            id = account?.id ?: "",
                            platformName = platformName,
                            username = username.ifEmpty { null },
                            email = email.ifEmpty { null },
                            password = password
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                enabled = platformName.isNotBlank() && password.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}