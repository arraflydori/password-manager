package com.arraflydori.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arraflydori.passwordmanager.domain.Tag
import com.arraflydori.passwordmanager.domain.TagRepository
import com.arraflydori.passwordmanager.domain.Vault
import com.arraflydori.passwordmanager.domain.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VaultDetailUiState(
    val vault: Vault = Vault(),
    val tags: List<Tag> = listOf(),
    val saveSuccess: Boolean? = null,
    val deleteSuccess: Boolean? = null,
) {
    val canDelete: Boolean = vault.id.isNotEmpty()
    val canSave: Boolean = vault.name.isNotBlank()
            && tags.isNotEmpty()
            && tags.all { it.label.isNotBlank() }
}

class VaultDetailViewModel(
    vaultId: String?,
    private val vaultRepository: VaultRepository,
    private val tagRepository: TagRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VaultDetailUiState())
    val uiState = _uiState.asStateFlow()
    private var oldTags: List<Tag>? = vaultId?.let { tagRepository.getTags(it) }

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    vault = vaultId?.let { vaultRepository.getVault(vaultId) } ?: Vault(),
                    tags = oldTags ?: listOf()
                )
            }
            if (_uiState.value.tags.isEmpty()) createTag()
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

    fun createTag() {
        _uiState.update {
            it.copy(tags = it.tags.toMutableList().apply {
                add(Tag(id = "", label = ""))
            })
        }
    }

    fun updateTag(index: Int, tagLabel: String) {
        _uiState.update {
            it.copy(tags = it.tags.toMutableList().apply {
                this[index] = this[index].copy(label = tagLabel)
            })
        }
    }

    fun removeTag(tag: Tag) {
        _uiState.update {
            it.copy(tags = it.tags.toMutableList().apply {
                remove(tag)
            })
        }
    }

    fun deleteVault() {
        viewModelScope.launch {
            vaultRepository.deleteVault(_uiState.value.vault.id)
            _uiState.update {
                it.copy(deleteSuccess = true)
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            _uiState.update {
                val vault = vaultRepository.updateVault(it.vault)
                if (vault != null) {
                    oldTags?.let { tags ->
                        tags.filter { tag -> it.tags.none { it.id == tag.id } }
                            .forEach { tag -> tagRepository.deleteTag(vault.id, tag) }
                    }
                    tagRepository.updateTags(vault.id, it.tags)
                    it.copy(saveSuccess = true)
                } else {
                    it.copy(saveSuccess = false)
                }
            }
        }
    }
}