package com.arraflydori.passwordmanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val platformName: String,
    val username: String?,
    val email: String?,
    val password: String,
)