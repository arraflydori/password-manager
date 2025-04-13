package com.arraflydori.passwordmanager.model

class FakeTagRepository : TagRepository {
    private val tagMap = mutableMapOf<String, MutableList<Tag>>()
    private var idCounter = 0

    override fun getTags(vaultId: String): List<Tag> {
        return tagMap.getOrPut(vaultId) { mutableListOf() }
    }

    override fun updateTag(vaultId: String, tag: Tag): Boolean {
        val tags = tagMap.getOrPut(vaultId) { mutableListOf() }
        return if (tags.indexOfFirst { it.label == tag.label } != -1) {
            false
        } else {
            if (tag.id.isBlank()) {
                tags.add(tag.copy(id = (++idCounter).toString()))
                true
            } else {
                val index = tags.indexOfFirst { it.id == tag.id }
                if (index == -1) {
                    tags.add(tag)
                    false
                } else {
                    tags[index] = tag
                    true
                }
            }
        }
    }

    override fun deleteTag(vaultId: String, tag: Tag): Boolean {
        return tagMap[vaultId]?.remove(tag) == true
    }
}
