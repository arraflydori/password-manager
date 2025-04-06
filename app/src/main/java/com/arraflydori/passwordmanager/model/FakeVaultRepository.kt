package com.arraflydori.passwordmanager.model

class FakeVaultRepository : VaultRepository {
    private val vaults = mutableListOf<Vault>()
    private var idCounter = 0

    override fun getAllVault(): List<Vault> {
        return vaults
    }

    override fun updateVault(vault: Vault): Boolean {
        return if (vault.id.isBlank()) {
            vaults.add(vault.copy(id = (++idCounter).toString()))
            true
        } else {
            val index = vaults.indexOfFirst { it.id == vault.id }
            if (index == -1) {
                vaults.add(vault)
                false
            } else {
                vaults[index] = vault
                true
            }
        }
    }

    override fun deleteVault(id: String): Boolean {
        return vaults.removeIf { it.id == id }
    }
}
