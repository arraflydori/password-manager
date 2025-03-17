package com.arraflydori.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
data class AccountDetail(val id: String)

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AccountList) {
        composable<AccountList> {
            AccountListScreen(
                accounts = sampleAccounts,
                onAccountClick = { account ->
                    navController.navigate(AccountDetail(account.id))
                }
            )
        }
        composable<AccountDetail> {
            val route = it.toRoute<AccountDetail>()
            AccountDetailScreen(
                account = sampleAccounts.first { it.id == route.id },
                onSave = {},
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
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