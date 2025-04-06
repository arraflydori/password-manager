package com.arraflydori.passwordmanager.model

interface VaultRepository {
    fun getAllVault(): List<Vault>
    fun updateVault(vault: Vault): Boolean
    fun deleteVault(id: String): Boolean
}