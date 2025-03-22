package com.arraflydori.passwordmanager.model

class FakeAccountRepository : AccountRepository {
    private val accounts = mutableListOf<Account>()
    private val tags = mutableSetOf<String>()
    private var idCounter = 0

    init {
        tags.addAll(listOf(
            "Service", "Finance", "Education", "Government", "Tech", "Social", "Lifestyle", "Entertainment"
        ))
    }

    override fun getAllAccounts(): List<Account> {
        return accounts
    }

    override fun getAccount(id: String): Account? {
        return accounts.firstOrNull { it.id == id }
    }

    override fun updateAccount(account: Account): Boolean {
        return if (account.id.isBlank()) {
            accounts.add(account.copy(
                id = (++idCounter).toString()
            ))
            true
        } else {
            val index =  accounts.indexOfFirst { it.id == account.id }
            if (index == -1) {
                accounts.add(account)
                false
            } else {
                accounts[index] = account
                true
            }
        }
    }

    override fun deleteAccount(id: String): Boolean {
        return accounts.removeIf { it.id == id }
    }

    override fun getAllTags(): Set<String> {
        return tags
    }

    override fun createTag(tag: String): Boolean {
        return tags.add(tag)
    }

    override fun deleteTag(tag: String): Boolean {
        return tags.remove(tag)
    }
}