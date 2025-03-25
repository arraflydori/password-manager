package com.arraflydori.passwordmanager.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CredentialType {
    @SerialName("pin") PIN,
    @SerialName("password") Password
}

@Serializable
data class Credential(
    val id: String,
    val type: CredentialType,
    val value: String,
)