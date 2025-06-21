package com.arraflydori.passwordmanager

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arraflydori.passwordmanager.data.AppDatabase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

class App : Application() {
    val db: AppDatabase by lazy {
        System.loadLibrary("sqlcipher")

        // TODO: Store this hardcoded password in Keystore?
        val password = "admin123".toByteArray()
        val factory = SupportOpenHelperFactory(password)

        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "encrypted_app_database"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration(true)
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("debug123", "Database created")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d("debug123", "Database open")
                }
            })
            .build()
    }
}