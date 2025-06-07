package com.arraflydori.passwordmanager.data

import com.arraflydori.passwordmanager.domain.Vault
import com.arraflydori.passwordmanager.domain.VaultRepository
import kotlinx.datetime.Clock

class FakeVaultRepository : VaultRepository {
    private val vaults = mutableListOf<Vault>()
    private var idCounter = 0

    override suspend fun getVaults(): List<Vault> {
        return vaults
    }

    override suspend fun getVault(id: String): Vault? {
        return vaults.firstOrNull { it.id == id }
    }

    override suspend fun updateVault(vault: Vault): Vault? {
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

    override suspend fun deleteVault(id: String): Boolean {
        return vaults.removeIf { it.id == id }
    }
}
