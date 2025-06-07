package com.arraflydori.passwordmanager.data

import com.arraflydori.passwordmanager.domain.Tag
import com.arraflydori.passwordmanager.domain.TagRepository

class FakeTagRepository : TagRepository {
    private val tagMap = mutableMapOf<String, MutableList<Tag>>()
    private var idCounter = 0

    override fun getTags(vaultId: String): List<Tag> {
        return tagMap.getOrPut(vaultId) { mutableListOf() }
    }

    override fun updateTags(vaultId: String, tags: List<Tag>): Boolean {
        val currentTags = tagMap.getOrPut(vaultId) { mutableListOf() }

        // Simulate the updated tag list.
        val updatedMap = currentTags.associateBy { it.id }.toMutableMap()
        for (tag in tags) {
            val id = if (tag.id.isEmpty()) (++idCounter).toString() else tag.id
            updatedMap[id] = if (id == tag.id) tag else tag.copy(id)
        }

        // Check for unique labels across the updated state.
        val updatedLabels = updatedMap.values.map { it.label }
        if (updatedLabels.size != updatedLabels.toSet().size) return false

        // Apply the updates.
        tagMap[vaultId] = updatedMap.values.toMutableList()
        return true
    }

    override fun deleteTag(vaultId: String, tag: Tag): Boolean {
        return tagMap[vaultId]?.remove(tag) == true
    }
}
