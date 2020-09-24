package com.galamdring.idledev

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.galamdring.idledev.database.WorkerDAO
import com.galamdring.idledev.database.WorkerDatabase

class WorkerViewModelFactory(
    private val viewModel: WidgetViewModel,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WidgetsManager::class.java)) {
            return WidgetsManager(viewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}