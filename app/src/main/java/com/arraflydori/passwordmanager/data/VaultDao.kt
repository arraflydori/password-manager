package com.arraflydori.passwordmanager.data

import androidx.room.*

@Dao
interface VaultDao {
    @Query("SELECT * FROM vault")
    suspend fun getAll(): List<VaultEntity>

    @Query("SELECT * FROM vault WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): VaultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(vault: VaultEntity): Long

    @Delete
    suspend fun delete(vault: VaultEntity): Int
}
