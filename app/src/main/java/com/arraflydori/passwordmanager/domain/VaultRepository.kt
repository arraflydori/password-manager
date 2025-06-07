package com.arraflydori.passwordmanager.domain

interface VaultRepository {
    suspend fun getVaults(): List<Vault>
    suspend fun getVault(id: String): Vault?
    suspend fun updateVault(vault: Vault): Vault?
    suspend fun deleteVault(id: String): Boolean
}