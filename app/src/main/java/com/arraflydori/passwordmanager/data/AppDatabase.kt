package com.arraflydori.passwordmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [VaultEntity::class], version = 1)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vaultDao(): VaultDao
}