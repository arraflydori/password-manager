package com.arraflydori.passwordmanager.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String = "",
    val platformName: String = "",
    val username: String? = null,
    val email: String? = null,
    val note: String = "",
    val credentials: List<Credential> = listOf(),
    val tagIds: List<String> = listOf(),
)

@Serializable
data class Credential(
    val id: String,
    val type: CredentialType,
    val value: String,
)

@Serializable
enum class CredentialType {
    @SerialName("pin") PIN,
    @SerialName("password") Password
}

@Serializable
data class Tag(
    val id: String,
    val label: String,
)