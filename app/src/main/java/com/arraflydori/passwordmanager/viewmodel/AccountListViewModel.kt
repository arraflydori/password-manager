package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.domain.Account
import com.arraflydori.passwordmanager.domain.AccountRepository
import com.arraflydori.passwordmanager.domain.Tag
import com.arraflydori.passwordmanager.domain.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountListUiState(
    val vaultId: String,
    val accounts: List<Account> = listOf(),
    val tags: List<Tag> = listOf(),
    val filteredAccounts: List<Account> = listOf(),
    val selectedTagIds: Set<String> = setOf(),
    val search: String = "",
)

class AccountListViewModel(
    val vaultId: String,
    private val accountRepository: AccountRepository,
    private val tagRepository: TagRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountListUiState(vaultId))
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
                    if (state.selectedTagIds.isEmpty()) {
                        it
                    } else {
                        it.filter {
                            it.tagIds.any { it in state.selectedTagIds }
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

    fun toggleTagSelection(tag: Tag) {
        _uiState.update {
            it.copy(
                selectedTagIds = it.selectedTagIds.toMutableSet().apply {
                    if (this.contains(tag.id)) {
                        remove(tag.id)
                    } else {
                        add(tag.id)
                    }
                }
            )
        }
        search(_uiState.value.search)
    }
}