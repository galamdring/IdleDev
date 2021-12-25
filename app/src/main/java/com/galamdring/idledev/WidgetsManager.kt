package com.galamdring.idledev

import kotlin.time.ExperimentalTime

class WidgetsManager(
    private val viewModel: WidgetViewModel,
) : Runnable {
    @OptIn(ExperimentalTime::class)
    override fun run() {
        viewModel.produce()
        WidgetsFragment.postDelayed(this, WidgetsFragment.widgetProductionDelay)
    }
}
