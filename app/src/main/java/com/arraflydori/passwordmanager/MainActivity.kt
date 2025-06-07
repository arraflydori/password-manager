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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arraflydori.passwordmanager.data.FakeAccountRepository
import com.arraflydori.passwordmanager.data.FakeTagRepository
import com.arraflydori.passwordmanager.data.FakeVaultRepository
import com.arraflydori.passwordmanager.domain.AccountRepository
import com.arraflydori.passwordmanager.domain.TagRepository
import com.arraflydori.passwordmanager.domain.VaultRepository
import com.arraflydori.passwordmanager.ui.screen.AccountDetailScreen
import com.arraflydori.passwordmanager.ui.screen.AccountListScreen
import com.arraflydori.passwordmanager.ui.screen.VaultDetailScreen
import com.arraflydori.passwordmanager.ui.screen.VaultListScreen
import com.arraflydori.passwordmanager.ui.theme.PasswordManagerTheme
import com.arraflydori.passwordmanager.viewmodel.AccountDetailViewModel
import com.arraflydori.passwordmanager.viewmodel.AccountListViewModel
import com.arraflydori.passwordmanager.viewmodel.VaultDetailViewModel
import com.arraflydori.passwordmanager.viewmodel.VaultListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                App(
                    vaultRepository = FakeVaultRepository(),
                    accountRepository = FakeAccountRepository(),
                    tagRepository = FakeTagRepository(),
                )
            }
        }
    }
}

@Composable
fun App(
    vaultRepository: VaultRepository,
    accountRepository: AccountRepository,
    tagRepository: TagRepository,
) {
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
        NavHost(navController = navController, startDestination = VaultListRoute) {
            composable<AccountListRoute> {
                val route = it.toRoute<AccountListRoute>()
                val viewModel = viewModel {
                    AccountListViewModel(
                        vaultId = route.vaultId,
                        accountRepository = accountRepository,
                        tagRepository = tagRepository
                    )
                }
                AccountListScreen(
                    viewModel,
                    onAccountClick = { account ->
                        navController.navigate(
                            AccountDetailRoute(
                                vaultId = route.vaultId,
                                accountId = account.id
                            )
                        )
                    },
                    onSettingsClick = { vaultId ->
                        navController.navigate(VaultDetailRoute(vaultId))
                    },
                    onNewAccount = {
                        navController.navigate(
                            AccountDetailRoute(
                                vaultId = route.vaultId,
                                accountId = null
                            )
                        )
                    }
                )
            }
            composable<AccountDetailRoute>(
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
                val route = it.toRoute<AccountDetailRoute>()
                val viewModel = viewModel {
                    AccountDetailViewModel(
                        vaultId = route.vaultId,
                        accountId = route.accountId,
                        accountRepository = accountRepository,
                        tagRepository = tagRepository
                    )
                }
                AccountDetailScreen(
                    viewModel = viewModel,
                    onSaveSuccess = {
                        navController.safePopBackStack()
                    },
                    onBack = {
                        navController.safePopBackStack()
                    },
                )
            }
            composable<VaultListRoute> {
                val viewModel = viewModel {
                    VaultListViewModel(vaultRepository = vaultRepository)
                }
                VaultListScreen(
                    viewModel = viewModel,
                    onVaultAdd = {
                        navController.navigate(VaultDetailRoute(null))
                    },
                    onVaultClick = { vaultId ->
                        navController.navigate(AccountListRoute(vaultId))
                    }
                )
            }
            composable<VaultDetailRoute>(
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
                val route = it.toRoute<VaultDetailRoute>()
                val viewModel = viewModel {
                    VaultDetailViewModel(
                        vaultId = route.vaultId,
                        vaultRepository = vaultRepository,
                        tagRepository = tagRepository,
                    )
                }
                VaultDetailScreen(
                    viewModel = viewModel,
                    onSaveSuccess = {
                        navController.safePopBackStack()
                    },
                    onDeleteSuccess = {
                        navController.navigate(VaultListRoute) {
                            popUpTo(VaultListRoute) {
                                inclusive = true
                            }
                        }
                    },
                    onBack = {
                        navController.safePopBackStack()
                    },
                )
            }
        }
    }
}

fun NavHostController.safePopBackStack() {
    val currentRoute = this.currentBackStackEntry?.destination?.route
    val previousRoute = this.previousBackStackEntry?.destination?.route

    if (currentRoute != null && previousRoute != null) popBackStack()
}