package com.arraflydori.passwordmanager.data

import com.arraflydori.passwordmanager.domain.Vault

fun VaultEntity.toDomain(): Vault = Vault(
    id = id,
    name = name,
    description = description,
    lastUpdate = lastUpdate
)

fun Vault.toEntity(): VaultEntity = VaultEntity(
    id = id,
    name = name,
    description = description,
    lastUpdate = lastUpdate
)