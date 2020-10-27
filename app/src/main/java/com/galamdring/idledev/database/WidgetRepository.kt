package com.galamdring.idledev.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

class WidgetRepository(application: Application) {
    val defaultWidget = Widget(0, 0.0)
    private val widgetDao = WorkerDatabase.getInstance(application).widgetDao

    val widget: Widget
        get() = runBlocking { widgetDao.getWidget() ?: defaultWidget }
    val widgets: LiveData<Widget>
        get() = widgetDao.getFirst()

    fun insert(widget: Widget) {
        runBlocking { widgetDao.insert(widget) }
    }

    fun initializeDB() = runBlocking {
        if (!widgetDao.hasItems()) {
            widgetDao.insert(Widget(0L, 0.0))
        }
    }
}
