package com.example.streamwidetest.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.*

/**
 * Utility class for file-related operations.
 *
 * @property context The Android application context.
 */
class FileUtils(private val context: Context) {

    /**
     * Checks if a file extension corresponds to an image file type.
     *
     * @param extension The file extension to be checked.
     * @return `true` if the extension is associated with an image file (e.g., jpg, jpeg, png, gif),
     *         `false` otherwise.
     */
    fun isImageExtension(extension: String): Boolean {
        val imageExtensions = setOf("jpg", "jpeg", "png", "gif")
        return extension.toLowerCase() in imageExtensions
    }

    /**
     * Checks if a file extension corresponds to a video file type.
     *
     * @param extension The file extension to be checked.
     * @return `true` if the extension is associated with a video file (e.g., mp4, avi),
     *         `false` otherwise.
     */
    fun isVideoExtension(extension: String): Boolean {
        val videoExtensions = setOf("mp4", "avi")
        return extension.toLowerCase() in videoExtensions
    }

    /**
     * Opens an encrypted file using the Android Jetpack Security library.
     *
     * @param file The file to open.
     * @param masterKey The master key for encryption.
     * @return An EncryptedFile instance for the specified file.
     */
    fun openEncryptedFile(file: File, masterKey: MasterKey): EncryptedFile {
        return EncryptedFile.Builder(
            context,
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    /**
     * Creates a temporary video file with a unique name in the application's cache directory.
     *
     * @return A File object representing the temporary video file.
     */
    fun createTempVideoFile(): File {
        return File.createTempFile("temp_video", ".mp4", context.cacheDir).apply {
            deleteOnExit()
        }
    }

    /**
     * Copies data from an input stream to a specified output file.
     *
     * @param inputStream The input stream to copy data from.
     * @param outputFile The file to which the data will be copied.
     */
    fun copyInputStreamToFile(inputStream: InputStream, outputFile: File) {
        val outputStream = FileOutputStream(outputFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            outputStream.write(buffer, 0, length)
        }
        inputStream.close()
        outputStream.close()
    }

    /**
     * Data class representing file details, including the name and extension of a file.
     *
     * @property name The name of the file.
     * @property extension The file extension.
     */
    data class FileDetails(val name: String, val extension: String)

    /**
     * Extracts file details (name and extension) from a given URI.
     *
     * @param uri The URI of the file.
     * @return FileDetails object containing the file name and extension.
     */
    fun getFileDetails(uri: Uri): FileDetails {
        val contentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val mimeType = contentResolver.getType(uri) ?: ""
        val extension = mimeTypeMap.getExtensionFromMimeType(mimeType) ?: ""

        val cursor = contentResolver.query(uri, null, null, null, null)
        var name = ""

        cursor?.use {
            if (it.moveToFirst()) {
                val nameColumn = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                name = if (nameColumn != -1) {
                    it.getString(nameColumn)
                } else {
                    uri.lastPathSegment ?: ""
                }
            }
        }

        return FileDetails(name, extension)
    }
}
