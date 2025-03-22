package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.ui.composable.MyTextField
import com.arraflydori.passwordmanager.viewmodel.AccountListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    viewModel: AccountListViewModel,
    onAccountClick: (Account) -> Unit = {},
    onNewAccount: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts()
    }

    Scaffold(
        topBar = {
            Column {
                MyTextField(
                    value = uiState.search,
                    onValueChange = {
                        viewModel.search(it)
                    },
                    hint = "Search account",
                    trailing = {
                        if (uiState.search.isEmpty()) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search account",
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            IconButton(
                                onClick = {
                                    viewModel.search("")
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Search account",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp, 16.dp, 16.dp, 8.dp)
                        .fillMaxWidth()
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    val tags = uiState.tags.toList()
                    items(tags.size) { i ->
                        val tag = tags[i]
                        if (i > 0) Spacer(modifier = Modifier.width(4.dp))
                        FilterChip(
                            selected = uiState.selectedTags.contains(tag),
                            onClick = {
                                viewModel.toggleTagSelection(tag)
                            },
                            label = {
                                Text(tag)
                            }
                        )
                        if (i < uiState.tags.size - 1) Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onNewAccount,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add account")
            }
        }
    ) { contentPadding ->
        if (uiState.filteredAccounts.isEmpty()) {
            Text(
                if (uiState.accounts.isEmpty()) "No accounts yet." else "No accounts found.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            ) {
                items(uiState.filteredAccounts) { account ->
                    AccountItem(
                        account = account,
                        onClick = { onAccountClick(account) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(
    account: Account,
    onClick: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(percent = 40)
                )
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = account.platformName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            if (account.username != null || account.email != null) {
                Text(
                    text = combine(account.username, account.email, " \u2022 "),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.DarkGray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                if (showPassword) account.password else "****",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.DarkGray
                ),
                maxLines = 1,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
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

fun combine(s1: String?, s2: String?, combiner: String = ""): String {
    return when {
        !s1.isNullOrBlank() && !s2.isNullOrBlank() -> s1 + combiner + s2
        !s1.isNullOrBlank() -> s1
        !s2.isNullOrBlank() -> s2
        else -> ""
    }
}