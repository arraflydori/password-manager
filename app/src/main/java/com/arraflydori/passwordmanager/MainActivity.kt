package com.arraflydori.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.ui.screen.AccountDetailScreen
import com.arraflydori.passwordmanager.ui.screen.AccountListScreen
import com.arraflydori.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                App()
            }
        }
    }
}

@Serializable
object AccountList

@Serializable
data class AccountDetail(val id: String?)

@Composable
fun App() {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            focusManager.clearFocus()
        }
    ) {
        NavHost(navController = navController, startDestination = AccountList) {
            composable<AccountList> {
                AccountListScreen(
                    accounts = sampleAccounts,
                    onAccountClick = { account ->
                        navController.navigate(AccountDetail(account.id))
                    },
                    onNewAccount = {
                        navController.navigate(AccountDetail(null))
                    }
                )
            }
            composable<AccountDetail>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(200)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(200)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(200)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(200)
                    )
                }
            ) {
                val route = it.toRoute<AccountDetail>()
                AccountDetailScreen(
                    account = sampleAccounts.firstOrNull { it.id == route.id },
                    onSave = {
                        // TODO: Implement save account
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

val sampleAccounts = listOf(
    Account(
        id = "1",
        platformName = "Twitter",
        username = "johndoe",
        email = "john@example.com",
        password = "securepass"
    ),
    Account(
        id = "2",
        platformName = "Facebook",
        username = "janedoe",
        email = "jane@example.com",
        password = "secret123"
    ),
    Account(
        id = "3",
        platformName = "Instagram",
        username = null,
        email = "contact@company.com",
        password = "pass123"
    )
)