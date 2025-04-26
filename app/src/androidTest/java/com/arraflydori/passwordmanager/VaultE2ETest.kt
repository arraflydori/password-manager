package com.arraflydori.passwordmanager

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arraflydori.passwordmanager.model.AccountRepository
import com.arraflydori.passwordmanager.model.FakeAccountRepository
import com.arraflydori.passwordmanager.model.FakeTagRepository
import com.arraflydori.passwordmanager.model.FakeVaultRepository
import com.arraflydori.passwordmanager.model.Tag
import com.arraflydori.passwordmanager.model.TagRepository
import com.arraflydori.passwordmanager.model.Vault
import com.arraflydori.passwordmanager.model.VaultRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class VaultE2ETest {
    @get:Rule
    val rule = createComposeRule()

    lateinit var vaultRepository: VaultRepository
    lateinit var accountRepository: AccountRepository
    lateinit var tagRepository: TagRepository

    @Before
    fun setUp() {
        vaultRepository = FakeVaultRepository()
        accountRepository = FakeAccountRepository()
        tagRepository = FakeTagRepository()
    }

    @Test
    fun createVault_AddsVaultToList() {
        rule.apply {
            val name = "Test"
            val description =
                "Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet."
            val tags = ('a'..'e').map { it.toString() }
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithContentDescription("Add vault")
                .performClick()
            onNodeWithText("Name", useUnmergedTree = true)
                .onParent()
                .performTextInput(name)
            onNodeWithText("Description", useUnmergedTree = true)
                .onParent()
                .performTextInput(description)
            for (tag in tags) {
                onAllNodesWithText("Tag", useUnmergedTree = true).apply {
                    if (fetchSemanticsNodes().isEmpty()) {
                        onNodeWithContentDescription("Add tag").performClick()
                    }
                }
                    .onLast()
                    .performScrollTo()
                    .onParent()
                    .performTextInput(tag)
            }
            onNodeWithText("Save").performScrollTo().performClick()
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText(name).assertExists()
            onNodeWithText(description, substring = true).assertExists()
        }
    }

    @Test
    fun openVault_displaysCorrectContent() {
        vaultRepository.updateVault(Vault(id = "1", name = "Foo"))
        tagRepository.updateTags(
            vaultId = "1",
            tags = listOf(Tag(id = "a", label = "A"))
        )
        rule.apply {
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("Foo").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithText("A").assertExists()
            onNodeWithText("No accounts yet.").assertExists()
            onNodeWithContentDescription("Open vault settings").performClick()
            waitUntilExactlyOneExists(hasText("Vault"))
            onNodeWithText("Foo").assertExists()
            onNodeWithText("A").assertExists()
        }
    }

    @Test
    fun editVault_updatesVaultData() {
        vaultRepository.updateVault(Vault(id = "1", name = "Foo"))
        tagRepository.updateTags(
            vaultId = "1",
            tags = listOf(Tag(id = "a", label = "A"))
        )
        rule.apply {
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("Foo").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithContentDescription("Open vault settings").performClick()
            waitUntilExactlyOneExists(hasText("Vault"))
            onNodeWithText("Foo").performTextReplacement("FooBar")
            onNodeWithText("Description", useUnmergedTree = true)
                .onParent()
                .performTextInput("Lorem ipsum dolor.")
            onNodeWithText("A")
                .performTextReplacement("0")
            for (tag in (1..9)) {
                onNodeWithContentDescription("Add tag").performClick()
                onAllNodesWithText("Tag", useUnmergedTree = true)
                    .onLast()
                    .performScrollTo()
                    .onParent()
                    .performTextInput(tag.toString())
            }
            onNodeWithText("Save").performScrollTo().performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            for (tag in (0..9)) {
                onNode(hasScrollAction()).performScrollToIndex(tag)
                onNodeWithText(tag.toString()).assertExists()
            }
            onNodeWithContentDescription("Open vault settings").performClick()
            waitUntilExactlyOneExists(hasText("Vault"))
            onNodeWithText("FooBar").assertExists()
            onNodeWithText("Lorem ipsum dolor.").assertExists()
        }
    }

    @Test
    fun deleteVault_removesVaultFromList() {
        vaultRepository.updateVault(Vault(id = "1", name = "Foo"))
        tagRepository.updateTags(
            vaultId = "1",
            tags = listOf(Tag(id = "a", label = "A"))
        )
        rule.apply {
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("Foo").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithContentDescription("Open vault settings").performClick()
            waitUntilExactlyOneExists(hasText("Vault"))
            onNodeWithContentDescription("Delete vault").performClick()
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("Foo").assertDoesNotExist()
        }
    }
}