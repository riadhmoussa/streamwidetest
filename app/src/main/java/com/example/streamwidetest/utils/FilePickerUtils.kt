package com.example.streamwidetest.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


/**
 * Utility class for simplifying file picking operations in an Android application.
 *
 * @param activity The parent AppCompatActivity.
 * @param onFilePicked Callback function to handle the selected file URI.
 */
class FilePickerUtils(
    private val activity: AppCompatActivity,
    private val onFilePicked: (Uri) -> Unit
) {
    // Activity result launcher for requesting permissions
    private val permissionLauncher: ActivityResultLauncher<Array<String>> = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            openFilePicker()
        }
    }

    // Activity result launcher for the file picker intent
    private val filePickerLauncher: ActivityResultLauncher<Intent> = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedFileUri = data?.data
            if (selectedFileUri != null) {
                onFilePicked(selectedFileUri)
            }
        }
    }

    /**
     * Requests permission to access files and opens the file picker if granted.
     * Handles different permission requests based on Android version.
     */
    fun requestFilePickerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // This is for devices running Android 33 or above
            val permissions = arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            )

            val permissionsToRequest = permissions.filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (permissionsToRequest.isEmpty()) {
                openFilePicker()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else {
            // This is for devices running Android versions below 33
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            val permissionsToRequest = permissions.filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (permissionsToRequest.isEmpty()) {
                openFilePicker()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    /**
     * Opens the file picker to select a file, handling permissions and results.
     */
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        filePickerLauncher.launch(intent)
    }
}
