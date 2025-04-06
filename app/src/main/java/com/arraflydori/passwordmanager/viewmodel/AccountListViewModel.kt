package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import com.arraflydori.passwordmanager.model.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountListUiState(
    val accounts: List<Account> = listOf(),
    val tags: Set<String> = setOf(),
    val filteredAccounts: List<Account> = listOf(),
    val selectedTags: Set<String> = setOf(),
    val search: String = "",
)

class AccountListViewModel(
    val vaultId: String,
    private val accountRepository: AccountRepository,
    private val tagRepository: TagRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountListUiState())
    val uiState = _uiState.asStateFlow()

    fun loadAccounts() {
        _uiState.update {
            it.copy(
                accounts = accountRepository.getAccounts(vaultId),
                tags = tagRepository.getTags(vaultId)
            )
        }
        search(_uiState.value.search)
    }

    fun search(search: String) {
        _uiState.update { state ->
            state.copy(
                search = search,
                filteredAccounts = state.accounts.let {
                    if (state.selectedTags.isEmpty()) {
                        it
                    } else {
                        it.filter {
                            it.tags.any { it in state.selectedTags }
                        }
                    }
                }.filter {
                    it.platformName.contains(search)
                            || it.username?.contains(search) == true
                            || it.email?.contains(search) == true
                }
            )
        }
    }

    fun toggleTagSelection(tag: String) {
        _uiState.update {
            it.copy(
                selectedTags = it.selectedTags.toMutableSet().apply {
                    if (this.contains(tag)) {
                        remove(tag)
                    } else {
                        add(tag)
                    }
                }
            )
        }
        search(_uiState.value.search)
    }
}