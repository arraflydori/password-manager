package com.arraflydori.passwordmanager.model

import kotlinx.datetime.Clock

class FakeVaultRepository : VaultRepository {
    private val vaults = mutableListOf<Vault>()
    private var idCounter = 0

    override fun getVaults(): List<Vault> {
        return vaults
    }

    override fun getVault(id: String): Vault? {
        return vaults.firstOrNull { it.id == id }
    }

    // TODO: Support batch updates
    override fun updateVault(vault: Vault): Vault? {
        vault.copy(lastUpdate = Clock.System.now()).let {
            return if (it.id.isBlank()) {
                val vault = it.copy(id = (++idCounter).toString())
                vaults.add(vault)
                vault
            } else {
                val index = vaults.indexOfFirst { it.id == vault.id }
                if (index == -1) {
                    vaults.add(it)
                    null
                } else {
                    vaults[index] = it
                    it
                }
            }
        }
    }

    override fun deleteVault(id: String): Boolean {
        return vaults.removeIf { it.id == id }
    }
}
