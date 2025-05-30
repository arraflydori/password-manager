package com.arraflydori.passwordmanager.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Vault(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val lastUpdate: Instant? = null,
)