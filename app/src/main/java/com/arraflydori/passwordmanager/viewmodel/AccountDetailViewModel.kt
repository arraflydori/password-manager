package com.arraflydori.passwordmanager.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountDetailUiState(
    val account: Account = Account(),
    val tagOptions: Set<String> = setOf(),
    val showPassword: Boolean = false,
    val showTagOptions: Boolean = false,
    val saveSuccess: Boolean? = null,
    val error: Error = Error()
) {
    data class Error(val invalidEmail: Boolean = false)

    val canSave: Boolean = account.platformName.isNotBlank()
            && account.password.isNotBlank()
            && !error.invalidEmail
}

class AccountDetailViewModel(
    val accountRepository: AccountRepository,
    accountId: String?,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            val tagOptions = accountRepository.getAllTags()
            it.copy(
                account = accountId?.let { accountRepository.getAccount(it) }
                    ?: Account(
                        id = "",
                        tags = tagOptions.firstOrNull()?.let { setOf(it) } ?: setOf()
                    ),
                tagOptions = tagOptions
            )
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
                account = it.account.copy(
                    id = it.account.id,
                    platformName = platformName ?: it.account.platformName,
                    username = username ?: it.account.username,
                    email = email ?: it.account.email,
                    password = password ?: it.account.password,
                ),
                error = it.error.copy(
                    invalidEmail = (email ?: it.account.email)?.let {
                        if (it.isEmpty()) false else !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    } == true
                )
            )
        }
    }

    fun addTag(tag: String) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    tags = it.account.tags.toMutableSet().apply {
                        add(tag)
                    }
                )
            )
        }
    }

    fun removeTag(tag: String) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    tags = it.account.tags.toMutableSet().apply {
                        remove(tag)
                    }
                )
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(showPassword = !it.showPassword)
        }
    }

    fun toggleTagOptionsVisibility() {
        _uiState.update {
            it.copy(showTagOptions = !it.showTagOptions)
        }
    }

    fun save() {
        accountRepository.updateAccount(_uiState.value.account)
        _uiState.update {
            it.copy(saveSuccess = true)
        }
    }
}