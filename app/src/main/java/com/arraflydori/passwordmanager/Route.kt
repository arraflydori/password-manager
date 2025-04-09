package com.arraflydori.passwordmanager

import kotlinx.serialization.Serializable

@Serializable
data class AccountListRoute(val vaultId: String)

@Serializable
data class AccountDetailRoute(val vaultId: String, val accountId: String?)

@Serializable
data object VaultListRoute

@Serializable
data class VaultDetailRoute(val vaultId: String?)