package com.arraflydori.passwordmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arraflydori.passwordmanager.model.Vault
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultListScreen(
    onVaultClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vaults") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(vaults.size) { i ->
                val vault = vaults[i]
                VaultCard(
                    vault = vault,
                    modifier = Modifier.clickable { onVaultClick(vault.id) }
                )
            }
        }
    }
}

@Composable
fun VaultCard(vault: Vault, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        BoxWithConstraints {
            val maxHeight = this.constraints.maxHeight
            val maxHeightDp = with(LocalDensity.current) { maxHeight.toDp() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeightDp * 0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = (maxHeightDp - 16.dp) * 0.1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = vault.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vault.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = vault.lastUpdate?.let { formatRelativeTime(it) } ?: "Unknown",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

private fun formatRelativeTime(instant: Instant): String {
    val now = Clock.System.now()
    val duration = now - instant
    return when {
        duration < 1.minutes -> "Just now"
        duration < 60.minutes -> "${duration.inWholeMinutes}m ago"
        duration < 24.hours -> "${duration.inWholeHours}h ago"
        duration < 7.days -> "${duration.inWholeDays}d ago"
        duration < 30.days -> "${duration.inWholeDays / 7}w ago"
        duration < 365.days -> "${duration.inWholeDays / 30}mo ago"
        else -> "${duration.inWholeDays / 365}y ago"
    }
}

// TODO: Load Vaults from ViewModel
val vaults = listOf(
    Vault(
        id = "1",
        name = "Password Manager",
        description = "Stores all your secure passwordsStores all your secure passwordsStores all your secure passwordsStores all your secure passwordsStores all your secure passwordsStores all your secure passwords",
        lastUpdate = Clock.System.now().minus(1.days)
    ),
    Vault(
        id = "2",
        name = "Project",
        description = "Work in progress coding projects",
        lastUpdate = Clock.System.now().minus(2.days)
    ),
    Vault(
        id = "3",
        name = "Bookshelf",
        description = "Reading list and book notes",
        lastUpdate = Clock.System.now().minus(3.hours)
    ),
    Vault(
        id = "4",
        name = "Recipes",
        description = "Personal cookbook and meal ideas",
        lastUpdate = Clock.System.now().minus(5.days)
    ),
    Vault(
        id = "5",
        name = "Journal",
        description = "Daily personal journal entries",
        lastUpdate = Clock.System.now().minus(10.hours)
    ),
    Vault(
        id = "6",
        name = "Ideas",
        description = "Brainstorming notes and random ideas",
        lastUpdate = Clock.System.now().minus(30.minutes)
    ),
    Vault(
        id = "6",
        name = "Ideas",
        description = "Brainstorming notes and random ideas",
        lastUpdate = Clock.System.now().minus(30.minutes)
    ),
    Vault(
        id = "6",
        name = "Ideas",
        description = "Brainstorming notes and random ideas",
        lastUpdate = Clock.System.now().minus(30.minutes)
    ),
    Vault(
        id = "6",
        name = "Ideas",
        description = "Brainstorming notes and random ideas",
        lastUpdate = Clock.System.now().minus(30.minutes)
    ),
)
