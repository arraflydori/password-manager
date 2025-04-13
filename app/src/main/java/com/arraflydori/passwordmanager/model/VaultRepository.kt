package com.arraflydori.passwordmanager.model

interface VaultRepository {
    fun getVaults(): List<Vault>
    fun getVault(id: String): Vault?
    fun updateVault(vault: Vault): Vault?
    fun deleteVault(id: String): Boolean
}