package com.arraflydori.passwordmanager

import kotlinx.serialization.Serializable

@Serializable
object AccountListRoute

@Serializable
data class AccountDetailRoute(val id: String?)

@Serializable
data object VaultListRoute