package com.example.streamwidetest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a FileEntity in the database.
 *
 * @property fileId: The unique identifier for the file (auto-generated).
 * @property fileName: The name of the file.
 * @property filePath: The path to the file.
 * @property extension: The file extension (e.g., "txt", "jpg").
 */
@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val fileId: Long = 0,
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "extension") val extension: String
)
