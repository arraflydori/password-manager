package com.arraflydori.passwordmanager.model

interface TagRepository {
    fun getTags(vaultId: String): Set<String>
    fun createTag(vaultId: String, tag: String): Boolean
    fun deleteTag(vaultId: String, tag: String): Boolean
}