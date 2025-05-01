package com.arraflydori.passwordmanager

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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
import com.arraflydori.passwordmanager.model.Account
import com.arraflydori.passwordmanager.model.AccountRepository
import com.arraflydori.passwordmanager.model.Credential
import com.arraflydori.passwordmanager.model.CredentialType
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
class E2ETest {
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
    fun openVault_displayCorrectVault() {
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

    @Test
    fun openVault_showsEmptyTextIfNoAccountYet() {
        rule.apply {
            vaultRepository.apply {
                updateVault(Vault(id = "1", name = "TestVault"))
            }
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("TestVault").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithText("No accounts yet.").assertExists()
        }
    }

    @Test
    fun openVault_showsCurrentAccountList() {
        vaultRepository.apply {
            updateVault(Vault(id = "1", name = "TestVault"))
        }
        tagRepository.apply {
            updateTags(
                vaultId = "1",
                tags = (0..9).map { Tag(id = it.toString(), label = it.toString()) }
            )
        }
        val tags = tagRepository.getTags(vaultId = "1")
        accountRepository.apply {
            ('A'..'Z').forEach {
                updateAccount(
                    vaultId = "1", account = Account(
                        platformName = it.toString(),
                        username = "test",
                        email = "test@example.com",
                        tags = listOf(tags.random())
                    )
                )
            }
        }
        rule.apply {
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("TestVault").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            ('A'..'Z').forEachIndexed { i, c ->
                onNode(hasScrollToIndexAction()).performScrollToIndex(i)
                onNodeWithText(c.toString()).assertExists()
            }
        }
    }

    @Test
    fun createAccount_addsAccountToList() {
        rule.apply {
            vaultRepository.apply {
                updateVault(Vault(id = "1", name = "TestVault"))
            }
            tagRepository.apply {
                updateTags(
                    vaultId = "1",
                    tags = (0..9).map { Tag(id = it.toString(), label = it.toString()) }
                )
            }
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("TestVault").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithContentDescription("Add account").performClick()
            waitUntilExactlyOneExists(hasText("Account"))
            onNodeWithText("Platform", useUnmergedTree = true).onParent()
                .performTextInput("Instagram")
            onNodeWithText("Username", useUnmergedTree = true).onParent()
                .performTextInput("abcdefg")
            onNodeWithText("Email", useUnmergedTree = true).onParent()
                .performTextInput("test@example.com")
            onNodeWithText("Note", useUnmergedTree = true).onParent()
                .performTextInput("Lorem ipsum dolor sit amet")
            onNodeWithContentDescription("Add credential").performClick()
            onNodeWithText("Password", useUnmergedTree = true).onParent()
                .performTextInput("test1234")
            onNodeWithText("0").assertExists()
            onNodeWithContentDescription("Add tag").performScrollTo().performClick()
            (1..9).forEach {
                waitUntilExactlyOneExists(hasText("Select tags"))
                onNodeWithText(it.toString()).performClick()
            }
            waitUntilDoesNotExist(hasText("Select tags"), timeoutMillis = 3000)
            onNodeWithText("Save").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithText("Instagram").performClick()
            waitUntilExactlyOneExists(hasText("Account"))
            onNodeWithText("Instagram").assertExists()
            onNodeWithText("abcdefg").assertExists()
            onNodeWithText("test@example.com").assertExists()
            onNodeWithText("Lorem ipsum dolor sit amet").assertExists()
            onNodeWithContentDescription("Show password").performClick()
            onNodeWithText("test1234").assertExists()
            (0..9).forEach {
                onNodeWithText(it.toString()).assertExists()
            }
        }
    }

    @Test
    fun editAccount_updatesAccountData() {
        vaultRepository.apply {
            updateVault(Vault(id = "1", name = "TestVault"))
        }
        tagRepository.apply {
            updateTags(
                vaultId = "1",
                tags = (0..9).map { Tag(id = it.toString(), label = it.toString()) }
            )
        }
        val tags = tagRepository.getTags(vaultId = "1")
        val oldTag = tags.first()
        val newTag = tags.last()
        rule.apply {
            accountRepository.apply {
                updateAccount(
                    vaultId = "1", account = Account(
                        platformName = "Instagram",
                        username = "abcdefg",
                        email = "test@example.com",
                        note = "Lorem ipsum dolor sit amet",
                        credentials = listOf(
                            Credential(
                                id = "",
                                type = CredentialType.Password,
                                value = "test1234"
                            )
                        ),
                        tags = listOf(oldTag)
                    )
                )
            }
            setContent {
                App(
                    vaultRepository = vaultRepository,
                    accountRepository = accountRepository,
                    tagRepository = tagRepository,
                )
            }
            waitUntilExactlyOneExists(hasText("Vaults"))
            onNodeWithText("TestVault").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithText("Instagram").performClick()
            waitUntilExactlyOneExists(hasText("Account"))
            onNodeWithText("Instagram").performTextReplacement("Facebook")
            onNodeWithText("abcdefg").performTextReplacement("lorem")
            onNodeWithText("test@example.com").performTextReplacement("lorem@example.com")
            onNodeWithText("Lorem ipsum dolor sit amet").performTextReplacement("This is a new description")
            onNodeWithContentDescription("Show password").performClick()
            onNodeWithText("test1234").performTextReplacement("qwerty")
            onNodeWithContentDescription("Add tag").performClick()
            onNodeWithText(newTag.label).performClick()
            onAllNodesWithContentDescription("Remove tag").onFirst().performClick()
            onNodeWithText("Save").performClick()
            waitUntilExactlyOneExists(hasText("Search account"))
            onNodeWithText("Facebook").performClick()
            waitUntilExactlyOneExists(hasText("Account"))
            onNodeWithText("Facebook").assertExists()
            onNodeWithText("lorem").assertExists()
            onNodeWithText("lorem@example.com").assertExists()
            onNodeWithText("This is a new description").assertExists()
            onNodeWithContentDescription("Show password").performClick()
            onNodeWithText("qwerty").assertExists()
            onNodeWithText(newTag.label).assertExists()
        }
    }
}