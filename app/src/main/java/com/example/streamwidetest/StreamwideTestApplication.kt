package com.example.streamwidetest

import android.app.Application
import com.example.streamwidetest.viewmodel.ViewModelFactory

/**
 * Custom Application class for the TrituxTest application.
 *
 * This class is responsible for initializing and setting up application-wide components and resources.
 */
class StreamwideTestApplication : Application() {
    /**
     * Called when the application is first created.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize the ViewModelFactory with the database helper.
        ViewModelFactory.initialize(this)
    }
}
