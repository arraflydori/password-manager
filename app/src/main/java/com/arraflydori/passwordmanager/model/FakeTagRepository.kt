package com.arraflydori.passwordmanager.model

import kotlin.random.Random

class FakeTagRepository : TagRepository {
    private val tagMap = mutableMapOf<String, MutableSet<String>>()
    private val dummyTags = setOf(
        "Service",
        "Finance",
        "Education",
        "Government",
        "Tech",
        "Social",
        "Lifestyle",
        "Entertainment"
    )

    override fun getTags(vaultId: String): Set<String> {
        return tagMap.getOrPut(vaultId) {
            mutableSetOf<String>().apply {
                repeat(Random.nextInt(1, dummyTags.size)) {
                    add(dummyTags.random())
                }
            }
        }
    }

    override fun createTag(vaultId: String, tag: String): Boolean {
        val tags = tagMap.getOrPut(vaultId) {
            mutableSetOf<String>().apply {
                repeat(Random.nextInt(1, dummyTags.size)) {
                    add(dummyTags.random())
                }
            }
        }
        return tags.add(tag)
    }

    override fun deleteTag(vaultId: String, tag: String): Boolean {
        return tagMap[vaultId]?.remove(tag) == true
    }
}
