package com.galamdring.idledev.database

import android.app.Application
import androidx.lifecycle.LiveData

class WidgetRepository(private val application: Application) {
    val defaultWidget = Widget(0, 0.0)
    private val widgetDao = WorkerDatabase.getInstance(application).widgetDao

    fun setCount(double: Double) {
        widgets.value?.let { it.count = double }
    }

    val widget: Widget
        get() = widgetDao.getWidget() ?: defaultWidget
    val widgets: LiveData<Widget>
        get() = widgetDao.getFirst()

    fun insert(widget: Widget) {
        widgetDao.insert(widget)
    }

    fun initializeDB() {
        if (!widgetDao.hasItems()) {
            widgetDao.insert(Widget(0L, 0.0))
        }
    }
}
