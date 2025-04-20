package com.arraflydori.passwordmanager.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class TagRepositoryTest {
    lateinit var repository: TagRepository

    @Before
    fun setUp() {
        repository = FakeTagRepository()
    }

    @Test
    fun tagRepository_CreateMultipleTagsWithEmptyId_CreatesCorrectly() {
        val tagsToCreate = mutableListOf<Tag>()
        ('a'..'z').forEach { c ->
            tagsToCreate.add(Tag(id = "", label = c.toString()))
        }
        repository.updateTags(vaultId = "1", tags = tagsToCreate)
        val fetchedTags = repository.getTags(vaultId = "1")
        val fetchedLabels = fetchedTags.map { it.label }.toSet()
        val inputLabels = tagsToCreate.map { it.label }.toSet()
        assertEquals(inputLabels, fetchedLabels)
        fetchedTags.forEach { assertTrue(it.id.isNotBlank()) }
    }

    @Test
    fun tagRepository_CreateMultipleTagsWithNonEmptyId_CreatesCorrectly() {
        val tagsToCreate = mutableListOf<Tag>()
        ('a'..'z').forEachIndexed { i, c ->
            tagsToCreate.add(Tag(id = i.toString(), label = c.toString()))
        }
        repository.updateTags(vaultId = "1", tags = tagsToCreate)
        val fetchedTags = repository.getTags(vaultId = "1")
        for (tag in tagsToCreate) {
            assertTrue(fetchedTags.contains(tag))
        }
    }

    @Test
    fun tagRepository_UpdateMultipleTags_UpdatesCorrectly() {
        val tag0 = Tag(id = "0", label = "alpha")
        val tag1 = Tag(id = "1", label = "beta")
        repository.updateTags(vaultId = "1", tags = listOf(tag0, tag1))
        assertTrue(
            repository.updateTags(
                vaultId = "1",
                tags = listOf(tag0.copy(label = "gamma"), tag1.copy(label = "lorem"))
            )
        )
        val tagsAfter = repository.getTags(vaultId = "1")
        assertEquals("gamma", tagsAfter.find { it.id == tag0.id }?.label)
        assertEquals("lorem", tagsAfter.find { it.id == tag1.id }?.label)
    }

    @Test
    fun tagRepository_UpdateMultipleTagsLabelToExistingLabel_UpdatesCorrectly() {
        val tag0 = Tag(id = "0", label = "alpha")
        val tag1 = Tag(id = "1", label = "beta")
        repository.updateTags(vaultId = "1", tags = listOf(tag0, tag1))
        assertTrue(
            repository.updateTags(
                vaultId = "1",
                tags = listOf(tag0.copy(label = tag1.label), tag1.copy(label = tag0.label))
            )
        )
        val tagsAfter = repository.getTags(vaultId = "1")
        assertEquals(tag1.label, tagsAfter.find { it.id == tag0.id }?.label)
        assertEquals(tag0.label, tagsAfter.find { it.id == tag1.id }?.label)
    }

    @Test
    fun tagRepository_UpdateMultipleTagsWithSameLabels_Fails() {
        val tag1 = Tag(id = "1", label = "alpha")
        val tag2 = Tag(id = "2", label = "beta")
        repository.updateTags("1", listOf(tag1, tag2))
        val result = repository.updateTags("1", listOf(
            tag1.copy(label = "shared"),
            tag2.copy(label = "shared")
        ))
        assertFalse(result)
        val tags = repository.getTags("1")
        assertEquals(null, tags.firstOrNull { it.label == "shared" })
    }

    @Test
    fun tagRepository_UpdateWithEmptyList_ReturnsTrue() {
        assertTrue(repository.updateTags(vaultId = "1", tags = emptyList()))
    }

    @Test
    fun tagRepository_UpdateTagWithDuplicateLabel_Fails() {
        val tag1 = Tag(id = "1", label = "label_one")
        val tag2 = Tag(id = "2", label = "label_two")
        repository.updateTags(vaultId = "1", tags = listOf(tag1, tag2))
        val updateResult = repository.updateTags(
            vaultId = "1",
            tags = listOf(tag2.copy(label = "label_one"))
        )
        assertFalse(updateResult)
        val tagsAfter = repository.getTags(vaultId = "1")
        assertEquals("label_one", tagsAfter.find { it.id == "1" }?.label)
        assertEquals("label_two", tagsAfter.find { it.id == "2" }?.label)
    }

    @Test
    fun tagRepository_DeleteMultipleTags_DeletesCorrectly() {
        val tag0 = Tag(id = "0", label = "alpha")
        val tag1 = Tag(id = "1", label = "beta")
        repository.updateTags(vaultId = "1", tags = listOf(tag0, tag1))
        assertTrue(repository.deleteTag(vaultId = "1", tag = tag0))
        assertTrue(repository.deleteTag(vaultId = "1", tag = tag1))
        val tagsAfter = repository.getTags(vaultId = "1")
        assertFalse(tagsAfter.contains(tag0))
        assertFalse(tagsAfter.contains(tag1))
    }

    @Test
    fun tagRepository_DeleteNonexistentTag_ReturnsFalse() {
        val nonExistentTag = Tag(id = "999", label = "ghost")
        val result = repository.deleteTag(vaultId = "1", tag = nonExistentTag)
        assertFalse(result)
    }

    @Test
    fun tagRepository_GetTagsFromUnknownVault_ReturnsEmpty() {
        val tags = repository.getTags(vaultId = "ghostVault")
        assertTrue(tags.isEmpty())
    }
}
