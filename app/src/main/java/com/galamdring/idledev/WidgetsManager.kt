package com.galamdring.idledev

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlinx.coroutines.*

@ExperimentalTime
val Duration.Since
get() : Duration{
    val currentDuration = System.currentTimeMillis().milliseconds
    return currentDuration - this
}

class WidgetsManager(
    private val viewModel: WidgetViewModel,
) : Runnable {


    @ExperimentalTime
    override fun run() {

        viewModel.produce()
        WidgetsFragment.postDelayed(this, 500)

    }




}