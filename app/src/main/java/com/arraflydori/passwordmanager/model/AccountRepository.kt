package com.arraflydori.passwordmanager.model

interface AccountRepository {
    fun getAllAccounts(): List<Account>
    fun getAccount(id: String): Account?
    fun updateAccount(account: Account): Boolean
    fun deleteAccount(id: String): Boolean
    fun getAllTags(): Set<String>
    fun createTag(tag: String): Boolean
    fun deleteTag(tag: String): Boolean
}