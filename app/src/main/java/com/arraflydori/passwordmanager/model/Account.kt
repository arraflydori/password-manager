package com.arraflydori.passwordmanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String = "",
    val platformName: String = "",
    val username: String? = null,
    val email: String? = null,
    val credentials: List<Credential> = listOf(),
    val tags: Set<String> = setOf(),
)