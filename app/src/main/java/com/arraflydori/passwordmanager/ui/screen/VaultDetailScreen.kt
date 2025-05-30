package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.ui.composable.MyTextField
import com.arraflydori.passwordmanager.viewmodel.VaultDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VaultDetailScreen(
    viewModel: VaultDetailViewModel,
    onSaveSuccess: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess == true) onSaveSuccess()
    }

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess == true) onDeleteSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Vault")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (uiState.canDelete) {
                        IconButton(onClick = { viewModel.deleteVault() }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete vault")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
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
                    value = vault.name,
                    onValueChange = {
                        viewModel.update(name = it)
                    },
                    hint = "Name",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                    trailing = {
                        if (vault.name.isBlank()) {
                            Icon(
                                Icons.Default.PriorityHigh,
                                contentDescription = "Empty name",
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
                    value = vault.description,
                    onValueChange = {
                        viewModel.update(description = it)
                    },
                    hint = "Description",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Available Tags", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            viewModel.createTag()
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
                for ((i, tag) in tags.withIndex()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MyTextField(
                            value = tag.label,
                            onValueChange = {
                                viewModel.updateTag(i, it)
                            },
                            hint = "Tag",
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            trailing = {
                                if (tag.label.isBlank()) {
                                    Icon(
                                        Icons.Default.PriorityHigh,
                                        contentDescription = "Empty tag label",
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
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            enabled = tags.size > 1,
                            onClick = {
                                viewModel.removeTag(tag)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier.size(20.dp)
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