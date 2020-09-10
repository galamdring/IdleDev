package com.galamdring.idledev

import android.app.Application

interface UiUpdater {

    fun updateAmateurCount(count:String)
    fun updateAmateurSpeed(speed:String)
    fun updateNoviceCount(count:String)
    fun updateNoviceSpeed(speed:String)
    fun updateApprenticeCount(count:String)
    fun updateApprenticeSpeed(speed:String)
    fun updateWidgetCount(count:String)
    fun updateMasterCount(count: String)
    fun updateMasterSpeed(speed: String)
    fun updateButtons()
    fun postDelayed(runner: Runnable, delay:Long)
    fun getApplication(): Application?
}