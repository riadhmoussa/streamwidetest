package com.example.streamwidetest.repository

import com.example.streamwidetest.model.FileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for handling file-related operations.
 *
 * This interface defines methods to retrieve and insert file entities in a repository.
 */
interface Repository {

    /**
     * Retrieves a list of file entities from the repository.
     *
     * @return A flow of a list of file entities.
     */
    fun getFiles(): Flow<List<FileEntity>>

    /**
     * Inserts a file entity into the repository.
     *
     * @param file The file entity to be inserted.
     * @return A flow of a list of file entities after insertion.
     */
    fun insertFile(file: FileEntity): Flow<Unit>
}
