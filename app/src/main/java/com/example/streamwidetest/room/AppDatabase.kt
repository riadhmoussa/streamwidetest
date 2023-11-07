package com.example.streamwidetest.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.streamwidetest.model.FileEntity

/**
 * Represents the Room Database for the TrituxTest application.
 *
 * This database includes tables for various entities, such as FileEntity.
 *
 * @property fileDao The Data Access Object (DAO) for the FileEntity table.
 */
@Database(entities = [FileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the FileEntity table.
     *
     * @return An instance of the [FileDao] for interacting with the FileEntity table.
     */
    abstract fun fileDao(): FileDao
}
