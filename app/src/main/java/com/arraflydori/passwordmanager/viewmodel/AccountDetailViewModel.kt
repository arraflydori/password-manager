package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountDetailUiState(
    val account: Account = Account(),
    val showPassword: Boolean = false,
    val saveSuccess: Boolean? = null,
)

class AccountDetailViewModel(
    val accountRepository: AccountRepository,
    accountId: String?,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        accountId?.let {
            _uiState.update {
                it.copy(
                    account = accountRepository.getAccount(accountId) ?: Account(id = accountId)
                )
            }
        }
    }

    fun update(
        platformName: String? = null,
        username: String? = null,
        email: String? = null,
        password: String? = null,
    ) {
        _uiState.update {
            it.copy(
                account = Account(
                    id = it.account.id,
                    platformName = platformName ?: it.account.platformName,
                    username = username ?: it.account.username,
                    email = email ?: it.account.email,
                    password = password ?: it.account.password
                )
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(showPassword = !it.showPassword)
        }
    }

    fun save() {
        accountRepository.updateAccount(_uiState.value.account)
        _uiState.update {
            it.copy(saveSuccess = true)
        }
    }
}