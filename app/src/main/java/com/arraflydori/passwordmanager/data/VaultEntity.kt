package com.arraflydori.passwordmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "vault")
data class VaultEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "last_update") val lastUpdate: Instant? = null
)