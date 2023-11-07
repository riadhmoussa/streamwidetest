package com.example.streamwidetest.repository

import com.example.streamwidetest.model.FileEntity
import com.example.streamwidetest.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of the Repository interface for managing FileEntity records.
 *
 * This class provides methods for retrieving and inserting FileEntity records
 * using the Room database.
 *
 * @property appDatabase The Room database instance.
 */
class RepositoryImpl(private val appDatabase: AppDatabase) : Repository {

    /**
     * Retrieves a list of all FileEntity records from the database.
     *
     * @return A [Flow] of lists of FileEntity records.
     */
    override fun getFiles(): Flow<List<FileEntity>> = appDatabase.fileDao().getAllFiles()

    /**
     * Inserts a new FileEntity record into the database.
     *
     * @param file The FileEntity to be inserted.
     * @return A [Flow] emitting a single [Unit] value after insertion.
     */
    override fun insertFile(file: FileEntity): Flow<Unit> = flow {
        appDatabase.fileDao().insertFile(file)
        emit(Unit)
    }
}
