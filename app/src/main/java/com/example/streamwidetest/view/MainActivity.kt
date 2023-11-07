package com.example.streamwidetest.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.example.streamwidetest.utils.FileUtils
import com.example.streamwidetest.adapters.FileEntityAdapter
import com.example.streamwidetest.adapters.FileEntityClickListener
import com.example.streamwidetest.databinding.ActivityMainBinding
import com.example.streamwidetest.model.FileEntity
import com.example.streamwidetest.utils.FilePickerUtils
import com.example.streamwidetest.view.dialog.DisplayFileDialog
import com.example.streamwidetest.viewmodel.MainActivityViewModel
import com.example.streamwidetest.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity(), FileEntityClickListener {

    // ViewModel to manage file entities and interactions
    private val viewModel: MainActivityViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var filePickerUtils: FilePickerUtils
    private lateinit var masterKey: MasterKey
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileEntityAdapter
    private val fileUtils = FileUtils(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components and setup interactions
        initializeMasterKey()
        initializeViews()
        initializeViewModel()
        setupFilePicker()

    }

    /**
     * Initializes the cryptographic MasterKey for secure data storage using the AES256_GCM algorithm.
     */
    private fun initializeMasterKey() {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        masterKey = MasterKey.Builder(applicationContext)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()
    }

    /**
     * Sets up the file picker utility and handles file selection.
     */
    private fun setupFilePicker() {
        filePickerUtils = FilePickerUtils(this) { uri ->
            saveEncryptedFile(uri)
        }

        binding.fab.setOnClickListener {
            filePickerUtils.requestFilePickerPermission()
        }
    }

    /**
     * Initializes the ViewModel and observes file list updates.
     */
    private fun initializeViewModel() {
        lifecycleScope.launch {
            viewModel.fileListState.collectLatest { files ->
                adapter.updateData(files.toList())
            }
        }
    }

    /**
     * Initializes UI components such as the RecyclerView and its adapter.
     */
    private fun initializeViews() {
        recyclerView = binding.recyclerViewFiles
        adapter = FileEntityAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    /**
     * Saves an encrypted image file selected by the user.
     *
     * @param imageUri The URI of the selected image file.
     */
    private fun saveEncryptedFile(imageUri: Uri) {
        val fileDetails = fileUtils.getFileDetails(imageUri)
        val imageFilename = fileDetails.name

        val encryptedFile = EncryptedFile.Builder(
            applicationContext,
            File(filesDir, imageFilename),
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val imageInputStream = contentResolver.openInputStream(imageUri)

        writeFile(encryptedFile.openFileOutput(), imageInputStream) {
            viewModel.insertFile(FileEntity(fileName = fileDetails.name, filePath = imageFilename, extension = fileDetails.extension))
        }
    }

    /**
     * Creates an encrypted file for securely storing the image.
     *
     * @param filename The name of the image file.
     */
    /**
     * Writes data from an input stream to an output stream and invokes a callback upon completion.
     *
     * @param outputStream The output stream to write data to.
     * @param inputStream The input stream containing the data to be written.
     * @param callback A callback function to be executed after writing the data.
     */
    private fun writeFile(
        outputStream: FileOutputStream,
        inputStream: InputStream?,
        callback: () -> Unit
    ) {
        outputStream.use { output ->
            inputStream.use { input ->
                input?.let {
                    val buffer = ByteArray(4 * 1024)
                    while (true) {
                        val byteCount = input.read(buffer)
                        if (byteCount < 0) break
                        output.write(buffer, 0, byteCount)
                    }
                    output.flush()
                    callback()
                }
            }
        }
    }

    /**
     * Handles the click event on a FileEntity item in the RecyclerView.
     * Opens an ImageDialog to display the selected file securely.
     *
     * @param file The FileEntity representing the selected file to display.
     */
    override fun onItemClick(file: FileEntity) {
        val dialog = DisplayFileDialog(this, file, masterKey)
        dialog.show()
    }

}


