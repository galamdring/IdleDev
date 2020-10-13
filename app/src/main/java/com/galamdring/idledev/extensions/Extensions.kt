package com.galamdring.idledev.extensions

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@ExperimentalTime
val Duration.Since
    get(): Duration
    {
        val currentDuration = System.currentTimeMillis().milliseconds
        return currentDuration - this
    }
