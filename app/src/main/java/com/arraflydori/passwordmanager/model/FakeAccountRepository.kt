package com.arraflydori.passwordmanager.model

class FakeAccountRepository : AccountRepository {
    val accounts = mutableListOf<Account>()

    init {
        accounts.addAll(
            listOf(
                Account(
                    id = "1",
                    platformName = "Twitter",
                    username = "johndoe",
                    email = "john@example.com",
                    password = "securepass"
                ),
                Account(
                    id = "2",
                    platformName = "Facebook",
                    username = "janedoe",
                    email = "jane@example.com",
                    password = "secret123"
                ),
                Account(
                    id = "3",
                    platformName = "Instagram",
                    username = null,
                    email = "contact@company.com",
                    password = "pass123"
                )
            )
        )
    }

    override fun getAllAccounts(): List<Account> {
        return accounts
    }

    override fun getAccount(id: String): Account? {
        return accounts.firstOrNull { it.id == id }
    }

    override fun updateAccount(account: Account): Boolean {
        val index = accounts.indexOfFirst { it.id == account.id }
        return if (index == -1) {
            accounts.add(account)
            false
        } else {
            accounts[index] = account
            true
        }
    }

    override fun deleteAccount(id: String): Boolean {
        return accounts.removeIf { it.id == id }
    }
}