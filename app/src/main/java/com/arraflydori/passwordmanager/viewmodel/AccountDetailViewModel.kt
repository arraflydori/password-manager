package com.arraflydori.passwordmanager.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import com.arraflydori.passwordmanager.model.Credential
import com.arraflydori.passwordmanager.model.CredentialType
import com.arraflydori.passwordmanager.model.Tag
import com.arraflydori.passwordmanager.model.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountDetailUiState(
    val account: Account = Account(),
    val tags: List<Tag> = listOf(),
    val tagOptions: List<Tag> = listOf(),
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
            val account = accountId?.let { accountRepository.getAccount(vaultId, it) }
                ?: Account(
                    id = "",
                    tagIds = tagOptions.firstOrNull()?.let { listOf(it.id) } ?: listOf(),
                )
            it.copy(
                account = account,
                tags = tagOptions.filter { it.id in account.tagIds },
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

    fun addTag(tag: Tag) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    tagIds = it.account.tagIds.toMutableList().apply {
                        add(tag.id)
                    }
                ),
                tags = it.tags.toMutableList().apply {
                    add(tag)
                }
            )
        }
    }

    fun removeTag(tag: Tag) {
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    tagIds = it.account.tagIds.toMutableList().apply {
                        remove(tag.id)
                    }
                ),
                tags = it.tags.toMutableList().apply {
                    remove(tag)
                }
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