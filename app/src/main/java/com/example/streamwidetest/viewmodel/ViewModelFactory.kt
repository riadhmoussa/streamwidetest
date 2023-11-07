package com.example.streamwidetest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streamwidetest.repository.RepositoryImpl
import com.example.streamwidetest.room.DatabaseBuilder

/**
 * A ViewModelFactory responsible for creating and providing ViewModels.
 *
 * @param application The application context.
 */
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    companion object {
        private var instance: ViewModelFactory? = null

        /**
         * Initializes the ViewModelFactory with the given application context.
         *
         * @param application The application context to initialize the factory.
         */
        fun initialize(application: Application) {
            synchronized(this) {
                if (instance == null) {
                    instance = ViewModelFactory(application)
                }
            }
        }

        /**
         * Retrieves the instance of the ViewModelFactory.
         *
         * @return The ViewModelFactory instance.
         * @throws ExceptionInInitializerError if the factory is not initialized using 'initialize'.
         */
        fun getInstance(): ViewModelFactory {
            return instance ?: throw ExceptionInInitializerError("You must initialize the factory with 'fun initialize(Application)'.")
        }
    }

    // Lazily initialize the repository using the application context
    private val repository by lazy { RepositoryImpl(DatabaseBuilder.getInstance(application)) }

    /**
     * Creates a ViewModel of the specified class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return The ViewModel instance.
     * @throws IllegalArgumentException if the requested ViewModel class is not recognized.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainActivityViewModel::class.java -> MainActivityViewModel(repository) as T
            else -> throw IllegalArgumentException("No class found for '${modelClass.name}'")
        }
    }
}
