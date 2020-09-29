package com.galamdring.idledev

import kotlin.time.ExperimentalTime

class WidgetsManager(
    private val viewModel: WidgetViewModel,
) : Runnable {
    @ExperimentalTime
    override fun run() {
        viewModel.produce()
        WidgetsFragment.postDelayed(this, 500)
    }
}
