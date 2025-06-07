package com.arraflydori.passwordmanager.data

import com.arraflydori.passwordmanager.domain.Account
import com.arraflydori.passwordmanager.domain.AccountRepository

class FakeAccountRepository : AccountRepository {
    private val accountMap = mutableMapOf<String, MutableList<Account>>()
    private var idCounter = 0

    override fun getAccounts(vaultId: String): List<Account> {
        return accountMap[vaultId].orEmpty()
    }

    override fun getAccount(vaultId: String, accountId: String): Account? {
        return accountMap[vaultId]?.firstOrNull { it.id == accountId }
    }

    override fun updateAccount(vaultId: String, account: Account): Boolean {
        val accounts = accountMap.getOrPut(vaultId) { mutableListOf() }

        return if (account.id.isBlank()) {
            accounts.add(account.copy(id = (++idCounter).toString()))
            true
        } else {
            val index = accounts.indexOfFirst { it.id == account.id }
            if (index == -1) {
                accounts.add(account)
                false
            } else {
                accounts[index] = account
                true
            }
        }
    }

    override fun deleteAccount(vaultId: String, accountId: String): Boolean {
        return accountMap[vaultId]?.removeIf { it.id == accountId } == true
    }
}
