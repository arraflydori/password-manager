package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.domain.Vault
import com.arraflydori.passwordmanager.domain.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VaultListUiState(
    val vaults: List<Vault> = listOf(),
)

class VaultListViewModel(
    private val vaultRepository: VaultRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VaultListUiState())
    val uiState = _uiState.asStateFlow()

    fun loadVaults() {
        _uiState.update {
            it.copy(vaults = vaultRepository.getVaults())
        }
    }
}