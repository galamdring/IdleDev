package com.galamdring.idledev

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.galamdring.idledev.database.WorkerDAO

class WorkerViewModelFactory(
    private val dataSource: WorkerDAO,
    private val application: Application,
    private val uiUpdater: UiUpdater
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WidgetsManager::class.java)) {
            return WidgetsManager(application, uiUpdater, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}