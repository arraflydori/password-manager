package com.arraflydori.passwordmanager.model

interface TagRepository {
    fun getTags(vaultId: String): List<Tag>
    fun updateTags(vaultId: String, tags: List<Tag>): Boolean
    fun deleteTag(vaultId: String, tag: Tag): Boolean
}