package com.galamdring.idledev

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerDAO
import com.galamdring.idledev.database.WorkerDatabase
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


@ExperimentalTime
val Duration.Since
get() : Duration{
    val currentDuration = System.currentTimeMillis().milliseconds
    return currentDuration - this
}

class WidgetsManager(
    application: Application,
    private val uiUpdater: UiUpdater,
    val database: WorkerDAO
) : Runnable, AndroidViewModel(application) {

    var amateurs = Workers(database.getWorker("amateur"), this)
    var novices = Workers(database.getWorker("novice"), this)
    var apprentices = Workers(database.getWorker("apprentice"), this)
    var masters = Workers(database.getWorker("master"), this)

    @ExperimentalTime
    var lastRun = System.currentTimeMillis().milliseconds


    private var wc = 0.0
    var widgetCount: Double
        get() = wc
        set(value) {
            this.wc = value
            uiUpdater.updateWidgetCount(helpers.formatNumbers(value))
        }


    var amateurCount: Double
        get() = amateurs.count
        set(value) {
            amateurs.count = value
            uiUpdater.updateAmateurCount(helpers.formatNumbers(value))
            uiUpdater.updateAmateurSpeed(helpers.formatNumbers(amateurs.totalSpeed()))
        }
    val amateurCountString:String
        get() = helpers.formatNumbers(amateurCount)

    var noviceCount: Double
    get() = novices.count
    set(value) {
        novices.count = value
        uiUpdater.updateNoviceCount(helpers.formatNumbers(value))
        uiUpdater.updateNoviceSpeed(helpers.formatNumbers(novices.totalSpeed()))
    }
    val noviceCountString:String
        get() = helpers.formatNumbers(noviceCount)

    var apprenticeCount: Double
        get() = apprentices.count
        set(value) {
            apprentices.count = value
            uiUpdater.updateApprenticeCount(helpers.formatNumbers(value))
            uiUpdater.updateApprenticeSpeed(helpers.formatNumbers(apprentices.totalSpeed()))
        }
    val apprenticeCountString:String
        get() = helpers.formatNumbers(apprenticeCount)

    var masterCount: Double
        get() = masters.count
        set(value) {
            masters.count = value
            uiUpdater.updateMasterCount(helpers.formatNumbers(value))
            uiUpdater.updateMasterSpeed(helpers.formatNumbers(apprentices.totalSpeed()))
        }

    val masterCountString: String
        get() = helpers.formatNumbers(masterCount)

    @ExperimentalTime
    override fun run() {
        widgetCount += amateurs.produce(lastRun.Since.inMilliseconds)
        amateurCount += novices.produce(lastRun.Since.inMilliseconds)
        noviceCount += apprentices.produce(lastRun.Since.inMilliseconds)
        apprenticeCount += masters.produce(lastRun.Since.inMilliseconds)
        uiUpdater.updateButtons()

        lastRun = System.currentTimeMillis().milliseconds

        uiUpdater.postDelayed(this, 500)

    }

    // item we currently own, and how much the new ones should cost.
    fun chargeForType(type: String,cost: Double): Boolean {
        when (type){
            "widgets" -> {
                if (cost <= wc) {
                    widgetCount -= cost;
                    return true;
                }
                return false
            }
 //           "dodad" -> {
 //               if (cost <= dodadCount) {
 //                   dodadCount -= cost;
 //                   return true;
 //               }
 //               return false
 //           }
            else -> return false;
        }
    }



    fun getDbWorkersOrCreateNew(){
        val application = uiUpdater.getApplication()
        amateurs = Workers(getDbWorker(application, "widgets", "amateur", 1.2, 0.0, 0, 1.2,
            10, 100.0, 1.1, 1000,), this)
        novices = Workers(getDbWorker(application, "widgets", "novice", 1.1, 0.0, 0, 1.2,
            10, 1000.0, 1.1, 1000), this)
        apprentices = Workers(getDbWorker(application, "widgets", "apprentice", .8, 0.0, 0, 1.2,
            10, 20000.0, 1.2, 1000), this)
        masters = Workers(getDbWorker(application, "widgets", "master", .7, 0.0, 0, 1.2,
            10, 1000000.0, 1.3, 1000), this)

    }

    fun getDbWorker(application: Application?,
                    name: String,
                    type: String,
                    speed: Double,
                    count: Double,
                    purchased: Int,
                    setBonus: Double,
                    setSize: Int,
                    cost: Double,
                    costIncrease: Double,
                    interval: Int) : Worker{
        val dataSource = WorkerDatabase.getInstance(application!!.applicationContext).workerDao
        var worker = dataSource.getWorker(type)
        if (worker==null){
            dataSource.insert(Worker(0, name, type, speed, count, purchased, setBonus,
                setSize, cost, costIncrease, interval))
            worker = dataSource.getWorker(type)
        }
        return worker
    }


}