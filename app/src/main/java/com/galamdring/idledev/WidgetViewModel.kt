package com.galamdring.idledev

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.galamdring.idledev.database.Widget
import com.galamdring.idledev.database.WidgetRepository
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerRepository
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class WidgetViewModel(application: Application) : AndroidViewModel(application) {

    private val workerRepository: WorkerRepository = WorkerRepository.getInstance(application)
    private val widgetRepository = WidgetRepository(application)

    val sharedPreferences = application.getSharedPreferences(
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
        get() = noviceManager.WorkerLive

    var apprenticeManager =
        WorkerManager.getWorker(WorkerManager.ApprenticeString, workerRepository)
    val apprenticesLive: MutableLiveData<Worker>
        get() = apprenticeManager.WorkerLive

    var amateurManager = WorkerManager.getWorker(WorkerManager.AmateurString, workerRepository)
    val amateursLive: MutableLiveData<Worker>
        get() = amateurManager.WorkerLive

    var journeymanManager =
        WorkerManager.getWorker(WorkerManager.JourneymanString, workerRepository)
    val journeymenLive: MutableLiveData<Worker>
        get() = journeymanManager.WorkerLive

    var masterManager = WorkerManager.getWorker(WorkerManager.MasterString, workerRepository)
    val mastersLive: MutableLiveData<Worker>
        get() = masterManager.WorkerLive

    var adeptManager = WorkerManager.getWorker(WorkerManager.AdeptString, workerRepository)
    val adeptsLive: MutableLiveData<Worker>
        get() = adeptManager.WorkerLive

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

    fun updateWorkers(workers: List<Worker>) {
        for (worker in workers) {
            WorkerManager.setWorker(worker)
        }
    }

    fun updateWidgets(widget: Widget) {
        widgets = widget
    }

    fun purchaseAmateurs(count: Int) {
        val cost = amateurManager.priceToCount(count)
        if (cost <= _widget.count && amateurManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
    }

    fun purchaseNovices(count: Int) {
        val cost = noviceManager.priceToCount(count)
        if (cost <= _widget.count && noviceManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
    }

    fun purchaseApprentices(count: Int) {
        val cost = apprenticeManager.priceToCount(count)
        if (cost <= _widget.count && apprenticeManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
    }

    fun purchaseMasters(count: Int) {
        val cost = masterManager.priceToCount(count)
        if (cost <= _widget.count && masterManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
    }

    fun purchaseJourneymen(count: Int) {
        val cost = journeymanManager.priceToCount(count)
        if (cost <= _widget.count && journeymanManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
    }

    fun purchaseAdepts(count: Int) {
        val cost = adeptManager.priceToCount(count)
        if (cost <= _widget.count && adeptManager.purchase(count)) {
            setWidgetCount(_widget.count - cost)
        }
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

            with(savedInstanceState.getDouble(amateurCountSavedInstanceKey)) {
                if (this > 0) amateurManager.count = this
            }
            with(savedInstanceState.getDouble(amateurCostSavedInstanceKey)) {
                if (this > 0) amateurManager.cost = this
            }
            with(savedInstanceState.getInt(amateurPurchasedCountSavedInstanceKey)) {
                if (this > 0) amateurManager.purchasedCount = this
            }

            with(savedInstanceState.getDouble(noviceCountSavedInstanceKey)) {
                if (this > 0) noviceManager.count = this
            }
            with(savedInstanceState.getDouble(noviceCostSavedInstanceKey)) {
                if (this > 0) noviceManager.cost = this
            }
            with(savedInstanceState.getInt(novicePurchasedCountSavedInstanceKey)) {
                if (this > 0) noviceManager.purchasedCount = this
            }

            with(savedInstanceState.getDouble(apprenticeCountSavedInstanceKey)) {
                if (this > 0) apprenticeManager.count = this
            }
            with(savedInstanceState.getDouble(apprenticeCostSavedInstanceKey)) {
                if (this > 0) apprenticeManager.cost = this
            }
            with(savedInstanceState.getInt(apprenticePurchasedCountSavedInstanceKey)) {
                if (this > 0) apprenticeManager.purchasedCount = this
            }

            with(savedInstanceState.getDouble(journeymenCountSavedInstanceKey)) {
                if (this > 0) journeymanManager.count = this
            }
            with(savedInstanceState.getDouble(journeymenCostSavedInstanceKey)) {
                if (this > 0) journeymanManager.cost = this
            }
            with(savedInstanceState.getInt(journeymenPurchasedCountSavedInstanceKey)) {
                if (this > 0) journeymanManager.purchasedCount = this
            }

            with(savedInstanceState.getDouble(masterCountSavedInstanceKey)) {
                if (this > 0) masterManager.count = this
            }
            with(savedInstanceState.getDouble(masterCostSavedInstanceKey)) {
                if (this > 0) masterManager.cost = this
            }
            with(savedInstanceState.getInt(masterPurchasedCountSavedInstanceKey)) {
                if (this > 0) masterManager.purchasedCount = this
            }

            with(savedInstanceState.getDouble(adeptCountSavedInstanceKey)) {
                if (this > 0) adeptManager.count = this
            }
            with(savedInstanceState.getDouble(adeptCostSavedInstanceKey)) {
                if (this > 0) adeptManager.cost = this
            }
            with(savedInstanceState.getInt(adeptPurchasedCountSavedInstanceKey)) {
                if (this > 0) adeptManager.purchasedCount = this
            }
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

    private fun addProducedWidgets(produced: Widget, count: Double): Widget {
        produced.count += count
        return produced
    }

    private fun addProducedWorker(worker: Worker, count: Double): Worker {
        worker.count += count
        return worker
    }

    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {
            R.id.widgetProduceButton -> produceWidget()
            R.id.amateurBuySet -> {
                purchaseAmateurs(amateurManager.countToSet)
            }
            R.id.amateurBuySingle -> {
                purchaseAmateurs(1)
            }
            R.id.noviceBuySingle -> {
                purchaseNovices(1)
            }
            R.id.noviceBuySet -> {
                purchaseNovices(noviceManager.countToSet)
            }
            R.id.apprenticeBuySingle -> {
                purchaseApprentices(1)
            }
            R.id.apprenticeBuySet -> {
                purchaseApprentices(apprenticeManager.countToSet)
            }
            R.id.journeymanBuySingle -> purchaseJourneymen(1)
            R.id.journeymanBuySet -> purchaseJourneymen(journeymanManager.countToSet)
            R.id.masterBuySingle -> {
                purchaseMasters(1)
            }
            R.id.masterBuySet -> {
                purchaseMasters(masterManager.countToSet)
            }
            R.id.adeptBuySingle -> purchaseAdepts(1)
            R.id.adeptBuySet -> purchaseAdepts(adeptManager.countToSet)
            R.id.resetButton -> resetAll()
        }
    }

    private fun resetAll() {
        WorkerManager.resetWorkers(workerRepository)
        setWidgetCount(0.0)
    }

    private fun resetWorker(worker: Worker): Worker {
        val resetWorker = workerRepository.defaultValues[worker.type]
            ?: error("worker type $worker.type not found")
        resetWorker.workerId = worker.workerId
        return resetWorker
    }

    fun saveAll() {
        widgetRepository.insert(_widget)
        WorkerManager.saveAll(workerRepository)
    }
}
