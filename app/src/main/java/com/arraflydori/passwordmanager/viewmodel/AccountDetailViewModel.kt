package com.arraflydori.passwordmanager.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import com.arraflydori.passwordmanager.model.Credential
import com.arraflydori.passwordmanager.model.CredentialType
import com.arraflydori.passwordmanager.model.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountDetailUiState(
    val account: Account = Account(),
    val tagOptions: Set<String> = setOf(),
    val showTagOptions: Boolean = false,
    val saveSuccess: Boolean? = null,
    val error: Error = Error()
) {
    data class Error(val invalidEmail: Boolean = false)

    val canSave: Boolean = account.platformName.isNotBlank()
            && account.credentials.all { it.value.isNotBlank() }
            && !error.invalidEmail
}

class AccountDetailViewModel(
    val vaultId: String,
    accountId: String?,
    private val accountRepository: AccountRepository,
    private val tagRepository: TagRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountDetailUiState())
    val uiState = _uiState.asStateFlow()
    private var tempCredId = 0

    init {
        _uiState.update {
            val tagOptions = tagRepository.getTags(vaultId)
            it.copy(
                account = accountId?.let { accountRepository.getAccount(vaultId, it) }
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
        note: String? = null,
    ) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    id = it.account.id,
                    platformName = platformName ?: it.account.platformName,
                    username = username ?: it.account.username,
                    email = email ?: it.account.email,
                    note = note ?: it.account.note,
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

    fun createCredential() {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    credentials = it.account.credentials.toMutableList().apply {
                        // TODO: Fix this Credential Id
                        add(
                            Credential(
                                id = tempCredId++.toString(),
                                type = CredentialType.Password,
                                value = ""
                            )
                        )
                    }
                )
            )
        }
    }

    fun updateCredential(id: String, value: String? = null, type: CredentialType? = null) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    credentials = it.account.credentials.toMutableList().apply {
                        val index = indexOfFirst { it.id == id }
                        if (index != -1) {
                            this[index] = this[index].copy(
                                value = value ?: this[index].value,
                                type = type ?: this[index].type
                            )
                        }
                    }
                )
            )
        }
    }

    fun removeCredential(credential: Credential) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    credentials = it.account.credentials.toMutableList().apply {
                        remove(credential)
                    }
                )
            )
        }
    }

    fun toggleTagOptionsVisibility() {
        _uiState.update {
            it.copy(showTagOptions = !it.showTagOptions)
        }
    }

    fun save() {
        accountRepository.updateAccount(vaultId, _uiState.value.account)
        _uiState.update {
            it.copy(saveSuccess = true)
        }
    }
}