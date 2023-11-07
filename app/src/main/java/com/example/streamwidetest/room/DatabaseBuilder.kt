package com.example.streamwidetest.room

import android.content.Context
import androidx.room.Room

/**
 * A singleton object responsible for creating and providing access to the Room Database for the TrituxTest application.
 *
 * This object ensures that only one instance of the database is created and used throughout the application's lifecycle.
 */
object DatabaseBuilder {

    private var INSTANCE: AppDatabase? = null

    /**
     * Retrieves or creates an instance of the Room Database.
     *
     * @param context The application context.
     * @return An instance of the [AppDatabase] that represents the Room Database.
     */
    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = buildRoomDB(context)
                }
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

}
