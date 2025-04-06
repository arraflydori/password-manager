package com.arraflydori.passwordmanager.model

class FakeTagRepository : TagRepository {
    private val tagMap = mutableMapOf<String, MutableSet<String>>()
    private val dummyTags = setOf(
        "Service", "Finance", "Education", "Government", "Tech", "Social", "Lifestyle", "Entertainment"
    )

    override fun getTags(vaultId: String): Set<String> {
        return tagMap.getOrPut(vaultId) { dummyTags.toMutableSet() }
    }

    override fun createTag(vaultId: String, tag: String): Boolean {
        val tags = tagMap.getOrPut(vaultId) { mutableSetOf() }
        return tags.add(tag)
    }

    override fun deleteTag(vaultId: String, tag: String): Boolean {
        return tagMap[vaultId]?.remove(tag) == true
    }
}
