package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Vault
import com.arraflydori.passwordmanager.model.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VaultDetailUiState(
    val vault: Vault = Vault(),
    val saveSuccess: Boolean? = null,
    val deleteSuccess: Boolean? = null,
) {
    val canDelete: Boolean = vault.id.isNotEmpty()
    val canSave: Boolean = vault.name.isNotBlank()
}

class VaultDetailViewModel(
    vaultId: String?,
    private val vaultRepository: VaultRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VaultDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(vault = vaultId?.let { vaultRepository.getVault(vaultId) } ?: Vault())
        }
    }

    fun update(
        name: String? = null,
        description: String? = null,
    ) {
        _uiState.update {
            it.copy(
                vault = it.vault.copy(
                    id = it.vault.id,
                    name = name ?: it.vault.name,
                    description = description ?: it.vault.description,
                )
            )
        }
    }

    fun delete() {
        vaultRepository.deleteVault(_uiState.value.vault.id)
        _uiState.update {
            it.copy(deleteSuccess = true)
        }
    }

    fun save() {
        vaultRepository.updateVault(_uiState.value.vault)
        _uiState.update {
            it.copy(saveSuccess = true)
        }
    }
}