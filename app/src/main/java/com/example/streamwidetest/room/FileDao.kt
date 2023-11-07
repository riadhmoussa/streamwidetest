package com.example.streamwidetest.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.streamwidetest.model.FileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for the FileEntity class.
 * The DAO defines the database operations for working with FileEntity records.
 */
@Dao
interface FileDao {
    /**
     * Inserts a new FileEntity record into the database.
     *
     * @param file The FileEntity to be inserted.
     */
    @Insert
    suspend fun insertFile(file: FileEntity)

    /**
     * Retrieves a list of all FileEntity records from the database.
     *
     * @return A [Flow] of lists of FileEntity records.
     */
    @Query("SELECT * FROM files")
    fun getAllFiles(): Flow<List<FileEntity>>

    /**
     * Retrieves a specific FileEntity record by its ID.
     *
     * @param id The ID of the FileEntity to retrieve.
     * @return The FileEntity with the specified ID.
     */
    @Query("SELECT * FROM files WHERE fileId = :id")
    suspend fun getFileById(id: Long): FileEntity

    /**
     * Updates an existing FileEntity record in the database.
     *
     * @param file The FileEntity to be updated.
     */
    @Update
    suspend fun updateFile(file: FileEntity)

    /**
     * Deletes a FileEntity record from the database.
     *
     * @param file The FileEntity to be deleted.
     */
    @Delete
    suspend fun deleteFile(file: FileEntity)
}
