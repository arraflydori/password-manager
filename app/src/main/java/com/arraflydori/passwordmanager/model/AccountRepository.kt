package com.arraflydori.passwordmanager.model

interface AccountRepository {
    fun getAllAccounts(): List<Account>
    fun getAccount(id: String): Account?
    fun updateAccount(account: Account): Boolean
    fun deleteAccount(id: String): Boolean
}