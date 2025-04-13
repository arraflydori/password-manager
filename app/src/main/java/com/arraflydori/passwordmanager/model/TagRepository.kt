package com.arraflydori.passwordmanager.model

interface TagRepository {
    fun getTags(vaultId: String): List<Tag>
    fun updateTag(vaultId: String, tag: Tag): Boolean
    fun deleteTag(vaultId: String, tag: Tag): Boolean
}