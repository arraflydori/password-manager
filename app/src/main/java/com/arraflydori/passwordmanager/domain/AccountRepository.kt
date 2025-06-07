package com.arraflydori.passwordmanager.domain

interface AccountRepository {
    fun getAccounts(vaultId: String): List<Account>
    fun getAccount(vaultId: String, accountId: String): Account?
    fun updateAccount(vaultId: String, account: Account): Boolean
    fun deleteAccount(vaultId: String, accountId: String): Boolean
}
