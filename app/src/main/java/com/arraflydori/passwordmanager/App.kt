package com.arraflydori.passwordmanager

import android.app.Application
import androidx.room.Room
import com.arraflydori.passwordmanager.data.AppDatabase

class App : Application() {
    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}