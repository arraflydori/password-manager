package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountListUiState(
    val accounts: List<Account> = listOf(),
    val filteredAccounts: List<Account> = listOf(),
    val search: String = "",
)

class AccountListViewModel(val accountRepository: AccountRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountListUiState())
    val uiState = _uiState.asStateFlow()

    fun loadAccounts() {
        _uiState.update {
            it.copy(accounts = accountRepository.getAllAccounts())
        }
    }

    fun search(search: String) {
        _uiState.update {
            it.copy(
                filteredAccounts = it.accounts.filter {
                    it.platformName.contains(search)
                            || it.username?.contains(search) == true
                            || it.email?.contains(search) == true
                }
            )
        }
    }
}