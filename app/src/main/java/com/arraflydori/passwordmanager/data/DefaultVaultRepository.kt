package com.arraflydori.passwordmanager.data

import com.arraflydori.passwordmanager.domain.Vault
import com.arraflydori.passwordmanager.domain.VaultRepository
import kotlinx.datetime.Clock

import java.util.UUID

class DefaultVaultRepository(
    private val dao: VaultDao
) : VaultRepository {

    override suspend fun getVaults(): List<Vault> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun getVault(id: String): Vault? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun updateVault(vault: Vault): Vault? {
        val updatedVault = vault.copy(
            id = if (vault.id.isBlank()) UUID.randomUUID().toString() else vault.id,
            lastUpdate = Clock.System.now()
        )
        dao.insertOrUpdate(updatedVault.toEntity())
        return getVault(updatedVault.id)
    }

    override suspend fun deleteVault(id: String): Boolean {
        val vault = dao.getById(id) ?: return false
        return dao.delete(vault) > 0
    }
}
