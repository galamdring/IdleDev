package com.galamdring.idledev

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.galamdring.idledev.database.Widget
import com.galamdring.idledev.database.WidgetRepository
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerRepository
import com.galamdring.idledev.extensions.Since
import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class WidgetViewModel(application: Application) : AndroidViewModel(application) {

    private var produceNow = true
    private val workerRepository: WorkerRepository = WorkerRepository.getInstance(application)
    private val widgetRepository = WidgetRepository(application)

    val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_key),
        Context.MODE_PRIVATE
    )

    val allWorkers: LiveData<List<Worker>>
        get() = workerRepository.allWorkers

    private var _widget = widgetRepository.widget
    var widgets: Widget
        get() = _widget
        set(value) {
            _widget = value
            widgetsLive.postValue(value)
        }
    var widgetsLive = MutableLiveData(_widget)
    private fun setWidgetCount(count: Double) {
        _widget.count = count
        widgetsLive.postValue(_widget)
    }

    var noviceManager = WorkerManager.getWorker(WorkerManager.NoviceString, workerRepository)
    val novicesLive: MutableLiveData<Worker>
        get() = noviceManager.workerLive

    var apprenticeManager =
        WorkerManager.getWorker(WorkerManager.ApprenticeString, workerRepository)
    val apprenticesLive: MutableLiveData<Worker>
        get() = apprenticeManager.workerLive

    var amateurManager = WorkerManager.getWorker(WorkerManager.AmateurString, workerRepository)
    val amateursLive: MutableLiveData<Worker>
        get() = amateurManager.workerLive

    var journeymanManager =
        WorkerManager.getWorker(WorkerManager.JourneymanString, workerRepository)
    val journeymenLive: MutableLiveData<Worker>
        get() = journeymanManager.workerLive

    var masterManager = WorkerManager.getWorker(WorkerManager.MasterString, workerRepository)
    val mastersLive: MutableLiveData<Worker>
        get() = masterManager.workerLive

    var adeptManager = WorkerManager.getWorker(WorkerManager.AdeptString, workerRepository)
    val adeptsLive: MutableLiveData<Worker>
        get() = adeptManager.workerLive

    val noviceBuySingleButtonEnabled = Transformations.map(novicesLive) { worker ->
        worker.cost < _widget.count
    }
    val noviceBuySetButtonEnabled = Transformations.map(novicesLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val amateurBuySingleButtonEnabled = Transformations.map(amateursLive) { worker ->
        worker.cost < _widget.count
    }
    val amateurBuySetButtonEnabled = Transformations.map(amateursLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val apprenticeBuySingleButtonEnabled = Transformations.map(apprenticesLive) { worker ->
        worker.cost < _widget.count
    }
    val apprenticeBuySetButtonEnabled = Transformations.map(apprenticesLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val journeymenBuySingleButtonEnabled = Transformations.map(journeymenLive) { worker ->
        worker.cost < _widget.count
    }
    val journeymenBuySetButtonEnabled = Transformations.map(journeymenLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val masterBuySingleButtonEnabled = Transformations.map(mastersLive) { worker ->
        worker.cost < _widget.count
    }
    val masterBuySetButtonEnabled = Transformations.map(mastersLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val adeptBuySingleButtonEnabled = Transformations.map(adeptsLive) { worker ->
        worker.cost < _widget.count
    }
    val adeptBuySetButtonEnabled = Transformations.map(adeptsLive) { worker ->
        worker.priceToSet() < _widget.count
    }

    val widgetCountString =
        Transformations.map(widgetsLive) { widget -> WidgetHelpers.formatNumbers(widget.count) }

    fun produceWidget() {
        setWidgetCount(widgets.count + 1)
    }

    @ExperimentalTime
    var lastRun = System.currentTimeMillis().milliseconds

    fun initDB() {
        widgetRepository.initializeDB()
        workerRepository.initializeDB()
    }

    private val widgetCountSavedInstanceKey = "widgetCount"
    private val amateurCountSavedInstanceKey = "amateurCount"
    private val apprenticeCountSavedInstanceKey = "apprenticeCount"
    private val noviceCountSavedInstanceKey = "noviceCount"
    private val masterCountSavedInstanceKey = "masterCount"
    private val journeymenCountSavedInstanceKey = "journeymenCount"
    private val adeptCountSavedInstanceKey = "adeptCount"

    private val amateurCostSavedInstanceKey = "amateurCostSavedInstanceKey"
    private val apprenticeCostSavedInstanceKey = "apprenticeCostSavedInstanceKey"
    private val noviceCostSavedInstanceKey = "noviceCostSavedInstanceKey"
    private val masterCostSavedInstanceKey = "masterCostSavedInstanceKey"
    private val journeymenCostSavedInstanceKey = "journeymenCostSavedInstanceKey"
    private val adeptCostSavedInstanceKey = "adeptCostSavedInstanceKey"

    private val amateurPurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"
    private val apprenticePurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"
    private val novicePurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"
    private val masterPurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"
    private val journeymenPurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"
    private val adeptPurchasedCountSavedInstanceKey = "amateurPurchasedCountSavedInstanceKey"

    fun loadState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {

            with(savedInstanceState.getDouble(widgetCountSavedInstanceKey)) {
                if (this > 0) setWidgetCount(this)
            }

            amateurManager.loadState(
                savedInstanceState.getDouble(amateurCountSavedInstanceKey),
                savedInstanceState.getDouble(amateurCostSavedInstanceKey),
                savedInstanceState.getInt(amateurPurchasedCountSavedInstanceKey)
            )

            noviceManager.loadState(
                savedInstanceState.getDouble(noviceCountSavedInstanceKey),
                savedInstanceState.getDouble(noviceCostSavedInstanceKey),
                savedInstanceState.getInt(novicePurchasedCountSavedInstanceKey)
            )

            apprenticeManager.loadState(
                savedInstanceState.getDouble(apprenticeCountSavedInstanceKey),
                savedInstanceState.getDouble(apprenticeCostSavedInstanceKey),
                savedInstanceState.getInt(apprenticePurchasedCountSavedInstanceKey)
            )

            journeymanManager.loadState(
                savedInstanceState.getDouble(journeymenCountSavedInstanceKey),
                savedInstanceState.getDouble(journeymenCostSavedInstanceKey),
                savedInstanceState.getInt(journeymenPurchasedCountSavedInstanceKey)
            )

            masterManager.loadState(
                savedInstanceState.getDouble(masterCountSavedInstanceKey),
                savedInstanceState.getDouble(masterCostSavedInstanceKey),
                savedInstanceState.getInt(masterPurchasedCountSavedInstanceKey)
            )

            adeptManager.loadState(
                savedInstanceState.getDouble(adeptCountSavedInstanceKey),
                savedInstanceState.getDouble(adeptCostSavedInstanceKey),
                savedInstanceState.getInt(adeptPurchasedCountSavedInstanceKey)
            )
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        // Save counts
        outState.putDouble(widgetCountSavedInstanceKey, widgets.count)

        outState.putDouble(amateurCountSavedInstanceKey, amateurManager.count)
        outState.putDouble(apprenticeCountSavedInstanceKey, apprenticeManager.count)
        outState.putDouble(noviceCountSavedInstanceKey, noviceManager.count)
        outState.putDouble(masterCountSavedInstanceKey, masterManager.count)
        outState.putDouble(journeymenCountSavedInstanceKey, journeymanManager.count)
        outState.putDouble(adeptCountSavedInstanceKey, adeptManager.count)
        // Save Costs
        outState.putDouble(amateurCostSavedInstanceKey, amateurManager.cost)
        outState.putDouble(apprenticeCostSavedInstanceKey, apprenticeManager.cost)
        outState.putDouble(noviceCostSavedInstanceKey, noviceManager.cost)
        outState.putDouble(masterCostSavedInstanceKey, masterManager.cost)
        outState.putDouble(journeymenCostSavedInstanceKey, journeymanManager.cost)
        outState.putDouble(adeptCostSavedInstanceKey, adeptManager.cost)
        // Save purchased counts
        outState.putInt(amateurPurchasedCountSavedInstanceKey, amateurManager.purchasedCount)
        outState.putInt(apprenticePurchasedCountSavedInstanceKey, apprenticeManager.purchasedCount)
        outState.putInt(novicePurchasedCountSavedInstanceKey, noviceManager.purchasedCount)
        outState.putInt(masterPurchasedCountSavedInstanceKey, masterManager.purchasedCount)
        outState.putInt(journeymenPurchasedCountSavedInstanceKey, journeymanManager.purchasedCount)
        outState.putInt(adeptPurchasedCountSavedInstanceKey, adeptManager.purchasedCount)
    }

    @ExperimentalTime
    fun produce() {
        if (produceNow) {
            widgets = addProducedWidgets(
                widgets,
                amateurManager.produce(lastRun.Since.inMilliseconds)
            )
            amateurManager.count += noviceManager.produce(lastRun.Since.inMilliseconds)

            noviceManager.count += apprenticeManager.produce(lastRun.Since.inMilliseconds)

            apprenticeManager.count += journeymanManager.produce(lastRun.Since.inMilliseconds)

            journeymanManager.count += masterManager.produce(lastRun.Since.inMilliseconds)

            masterManager.count += adeptManager.produce(lastRun.Since.inMilliseconds)

            // ping this count so it updates the livedata and button enabled
            adeptManager.count += 0.0

            lastRun = System.currentTimeMillis().milliseconds
        }
    }

    private fun addProducedWidgets(produced: Widget, count: Double): Widget {
        produced.count += count
        return produced
    }

    private fun purchaseWorker(worker: WorkerManager, count: Int) {
        /*
        val cost = worker.priceToCount(count)
        if (cost <= _widget.count && worker.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
        */
        runBlocking { Purchaser.purchaseWorker(worker, widgets, count) }
    }

    val clickListener = View.OnClickListener { view ->

        when (view.id) {
            R.id.widgetProduceButton -> produceWidget()
            R.id.amateurBuySet -> {
                purchaseWorker(amateurManager, amateurManager.countToSet)
            }
            R.id.amateurBuySingle -> {
                purchaseWorker(amateurManager, 1)
            }
            R.id.noviceBuySingle -> {
                purchaseWorker(noviceManager, 1)
            }
            R.id.noviceBuySet -> {
                purchaseWorker(noviceManager, noviceManager.countToSet)
            }
            R.id.apprenticeBuySingle -> {
                purchaseWorker(apprenticeManager, 1)
            }
            R.id.apprenticeBuySet -> {
                purchaseWorker(apprenticeManager, apprenticeManager.countToSet)
            }
            R.id.journeymanBuySingle -> purchaseWorker(journeymanManager, 1)
            R.id.journeymanBuySet -> purchaseWorker(journeymanManager, journeymanManager.countToSet)
            R.id.masterBuySingle -> {
                purchaseWorker(masterManager, 1)
            }
            R.id.masterBuySet -> {
                purchaseWorker(masterManager, masterManager.countToSet)
            }
            R.id.adeptBuySingle -> purchaseWorker(adeptManager, 1)
            R.id.adeptBuySet -> purchaseWorker(adeptManager, adeptManager.countToSet)
            R.id.resetButton ->
                resetAll()
        }
    }

    fun buyAll() {
        purchaseWorker(amateurManager, 1)
        purchaseWorker(noviceManager, 1)
        purchaseWorker(apprenticeManager, 1)
        purchaseWorker(journeymanManager, 1)
        purchaseWorker(masterManager, 1)
        purchaseWorker(adeptManager, 1)
    }

    private fun resetAll() {
        produceNow = false
        WorkerManager.resetWorkers(workerRepository)
        setWidgetCount(0.0)
        produceNow = true
    }

    fun saveAll() {
        widgetRepository.insert(_widget)
        Toast.makeText(getApplication(), "Game saved.", Toast.LENGTH_SHORT).show()
        WorkerManager.saveAll(workerRepository)
    }
}
