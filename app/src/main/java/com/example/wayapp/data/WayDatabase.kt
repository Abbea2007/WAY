package com.example.wayapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wayapp.model.ObjetoReportado

// 1. Declaramos que tablas pertenecen a esta db
@Database(entities = [ObjetoReportado::class], version = 1, exportSchema = false)
abstract class WayDatabase : RoomDatabase() {

    // 2. Conectamos nuestro DAO
    abstract fun objetoDao(): ObjetoDao

    // 3. Usamos un "Singleton" para asegurarnos de que solo exista UNA instancia
    // de la base de datos abierta al mismo tiempo en toda la app
    companion object {
        @Volatile
        private var INSTANCE: WayDatabase? = null

        fun getDatabase(context: Context): WayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WayDatabase::class.java,
                    "way_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}