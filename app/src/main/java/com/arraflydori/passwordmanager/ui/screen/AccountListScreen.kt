package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    accounts: List<Account>,
    onAccountClick: (Account) -> Unit = {}
) {
    var search by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BasicTextField(
                value = search,
                onValueChange = {
                    search = it
                    // TODO: Implement search
                },
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(percent = 40)
                            )
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            innerTextField()
                            if (search.isEmpty()) {
                                Text(
                                    "Search",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                containerColor = Color.LightGray,
                onClick = {
                    // TODO: Create Account
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add account")
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            items(accounts) { account ->
                AccountItem(
                    account = account,
                    onClick = { onAccountClick(account) }
                )
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
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (account.username != null || account.email != null) {
                Text(
                    text = combine(account.username, account.email, " \u2022 "),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
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