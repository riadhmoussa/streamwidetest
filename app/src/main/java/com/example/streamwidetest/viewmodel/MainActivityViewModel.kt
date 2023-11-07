package com.example.streamwidetest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamwidetest.model.FileEntity
import com.example.streamwidetest.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * ViewModel for the main activity, responsible for managing and providing data related to file entities.
 *
 * @param repository The repository responsible for interacting with data sources.
 */
class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    // A mutable state flow representing the list of file entities
    private val _fileListState = MutableStateFlow<List<FileEntity>>(emptyList())
    val fileListState: StateFlow<List<FileEntity>> get() = _fileListState

    /**
     * Initialize the ViewModel and fetch the list of files.
     */
    init {
        fetchFiles()
    }

    /**
     * Fetches the list of files from the repository and updates the state flow with the data.
     */
    private fun fetchFiles() {
        viewModelScope.launch {
            try {
                val files = repository.getFiles()
                files.flowOn(Dispatchers.IO).collect {
                    _fileListState.emit(it)
                    Log.i("MainActivityViewModel", "Files fetched successfully $it")
                }
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Error while fetching files: ${e.message}")
            }
        }
    }

    /**
     * Inserts a new file entity into the repository.
     *
     * @param file The file entity to be inserted.
     */
    fun insertFile(file: FileEntity) {
        viewModelScope.launch {
            try {
                repository.insertFile(file).collectLatest {
                    Log.i("MainActivityViewModel", "File inserted successfully")
                }
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Error while inserting file: ${e.message}")
            }
        }
    }

}
