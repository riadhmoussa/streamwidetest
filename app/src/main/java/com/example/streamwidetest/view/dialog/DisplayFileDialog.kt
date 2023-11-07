package com.example.streamwidetest.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.VideoView
import androidx.security.crypto.MasterKey
import com.example.streamwidetest.databinding.DialogDisplayFileBinding
import com.example.streamwidetest.utils.FileUtils
import com.example.streamwidetest.model.FileEntity
import java.io.File
import java.io.IOException

/**
 * Custom dialog for displaying image and video content securely within the application.
 *
 * @param context The Android context where the dialog will be displayed.
 * @param file The FileEntity representing the content to be displayed.
 * @param masterKey The master key used for file decryption.
 */
class DisplayFileDialog(context: Context, private val file: FileEntity, private val masterKey: MasterKey) : Dialog(context) {

    private lateinit var binding: DialogDisplayFileBinding
    private val fileUtils = FileUtils(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and set the dialog content
        binding = DialogDisplayFileBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        // Determine whether the content is an image or a video and display it accordingly
        when {
            fileUtils.isImageExtension(file.extension) -> {
                showImageContent(file.filePath, masterKey)
            }

            fileUtils.isVideoExtension(file.extension) -> {
                showVideoContent(file.filePath, masterKey)
            }
        }
    }

    /**
     * Displays image content in the dialog.
     *
     * @param contentFilename The file path to the encrypted image content.
     * @param masterKey The master key for file decryption.
     */
    private fun showImageContent(contentFilename: String, masterKey: MasterKey) {
        val file = File(context.filesDir, contentFilename)

        if (file.exists()) {
            val encryptedFile = fileUtils.openEncryptedFile(file, masterKey)
            val inputStream = encryptedFile.openFileInput()
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Display the image content in the dialog
            binding.imageView.visibility = View.VISIBLE
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    /**
     * Displays video content in the dialog.
     *
     * @param contentFilename The file path to the encrypted video content.
     * @param masterKey The master key for file decryption.
     */
    private fun showVideoContent(contentFilename: String, masterKey: MasterKey) {
        val file = File(context.filesDir, contentFilename)

        if (file.exists()) {
            val encryptedFile = fileUtils.openEncryptedFile(file, masterKey)
            val inputStream = encryptedFile.openFileInput()
            val videoView = VideoView(context)

            // Display the video content in the dialog
            binding.videoView.visibility = View.VISIBLE
            binding.rootContainer.addView(videoView)

            val tempVideoFile = fileUtils.createTempVideoFile()

            try {
                // Copy the encrypted video content to a temporary file
                fileUtils.copyInputStreamToFile(inputStream, tempVideoFile)

                // Play the video from the temporary file
                val videoUri = Uri.fromFile(tempVideoFile)
                videoView.setVideoURI(videoUri)
                videoView.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
