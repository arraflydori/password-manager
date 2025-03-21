package com.arraflydori.passwordmanager.model

class FakeAccountRepository : AccountRepository {
    val accounts = mutableListOf<Account>()

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