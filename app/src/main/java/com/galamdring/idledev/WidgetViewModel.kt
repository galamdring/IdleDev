package com.galamdring.idledev

import android.app.Application
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

    val allWorkers: LiveData<List<Worker>>
        get() = workerRepository.allWorkers

    private var _widget = widgetRepository.widget ?: widgetRepository.defaultWidget
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

    private var _novices = workerRepository.novices ?: workerRepository.defaultNovice
    var novices: Worker
        get() = _novices
        set(value) {
            _novices = value
            novicesLive.postValue(value)
        }
    var novicesLive: MutableLiveData<Worker> = MutableLiveData(_novices)
    private fun setNoviceCount(count: Double) {
        _novices.count = count
        novicesLive.postValue(_novices)
    }

    private var _apprentices = workerRepository.apprentices ?: workerRepository.defaultApprentice
    var apprentices: Worker
        get() = _apprentices
        set(value) {
            _apprentices = value
            apprenticesLive.postValue(value)
        }
    var apprenticesLive: MutableLiveData<Worker> = MutableLiveData(_apprentices)
    private fun setApprenticesCount(count: Double) {
        _apprentices.count = count
        apprenticesLive.postValue(_apprentices)
    }

    private var _amateurs = workerRepository.amateurs ?: workerRepository.defaultNovice
    var amateurs: Worker
        get() = _amateurs
        set(value) {
            _amateurs = value
            amateursLive.postValue(value)
        }
    var amateursLive: MutableLiveData<Worker> = MutableLiveData(_amateurs)
    private fun setAmateursCount(count: Double) {
        _amateurs.count = count
        amateursLive.postValue(_amateurs)
    }

    private var _journeymen = workerRepository.journeymen ?: workerRepository.defaultJourneyman
    var journeymen: Worker
        get() = _journeymen
        set(value) {
            _journeymen = value
            journeymenLive.postValue(value)
        }
    var journeymenLive: MutableLiveData<Worker> = MutableLiveData(_journeymen)
    private fun setJourneymenCount(count: Double) {
        _amateurs.count = count
        amateursLive.postValue(_amateurs)
    }

    private var _masters = workerRepository.masters ?: workerRepository.defaultMaster
    var masters: Worker
        get() = _masters
        set(value) {
            _masters = value
            mastersLive.postValue(value)
        }
    var mastersLive: MutableLiveData<Worker> = MutableLiveData(_masters)
    private fun setMastersCount(count: Double) {
        _masters.count = count
        mastersLive.postValue(_masters)
    }

    private var _adepts = workerRepository.adepts ?: workerRepository.defaultAdept
    var adepts: Worker
        get() = _adepts
        set(value) {
            _adepts = value
            adeptsLive.postValue(value)
        }
    var adeptsLive: MutableLiveData<Worker> = MutableLiveData(_adepts)
    private fun setadeptsCount(count: Double) {
        _adepts.count = count
        adeptsLive.postValue(_adepts)
    }

    private val widgetCountSavedInstanceKey = "widgetCount"
    private val amateurCountSavedInstanceKey = "amateurCount"
    private val apprenticeCountSavedInstanceKey = "apprenticeCount"
    private val noviceCountSavedInstanceKey = "noviceCount"
    private val masterCountSavedInstanceKey = "masterCount"
    private val journeymenCountSavedInstanceKey = "journeymenCount"
    private val adeptCountSavedInstanceKey = "adeptCount"

    val noviceCountString = Transformations.map(novicesLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val noviceCount = Transformations.map(novicesLive) { worker ->
        worker.count
    }
    val noviceTotalSpeedString = Transformations.map(novicesLive) { worker ->
        totalSpeedString(worker)
    }
    val noviceBuySingleButtonText = Transformations.map(novicesLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val noviceBuySingleButtonEnabled = Transformations.map(novicesLive) { worker ->
        worker.cost < _widget.count
    }
    val noviceBuySetButtonText = Transformations.map(novicesLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }

    val amateurCountString = Transformations.map(amateursLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val amateurCount = Transformations.map(amateursLive) { worker ->
        worker.count
    }
    val amateurBuySingleButtonText = Transformations.map(amateursLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val amateurBuySingleButtonEnabled = Transformations.map(amateursLive) { worker ->
        worker.cost < _widget.count
    }
    val amateurBuySetButtonText = Transformations.map(amateursLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }
    val amateurTotalSpeedString = Transformations.map(amateursLive) { worker ->
        totalSpeedString(worker)
    }

    val apprenticeCountString = Transformations.map(apprenticesLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val apprenticeCount = Transformations.map(apprenticesLive) { worker ->
        worker.count
    }
    val apprenticeBuySingleButtonText = Transformations.map(apprenticesLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val apprenticeBuySingleButtonEnabled = Transformations.map(apprenticesLive) { worker ->
        worker.cost < _widget.count
    }
    val apprenticeBuySetButtonText = Transformations.map(apprenticesLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }
    val apprenticeTotalSpeedString = Transformations.map(apprenticesLive) { worker ->
        totalSpeedString(worker)
    }

    val journeymenCountString = Transformations.map(journeymenLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val journeymenCount = Transformations.map(journeymenLive) { worker ->
        worker.count
    }
    val journeymenBuySingleButtonText = Transformations.map(journeymenLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val journeymenBuySingleButtonEnabled = Transformations.map(journeymenLive) { worker ->
        worker.cost < _widget.count
    }
    val journeymenBuySetButtonText = Transformations.map(journeymenLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }
    val journeymenTotalSpeedString = Transformations.map(journeymenLive) { worker ->
        totalSpeedString(worker)
    }

    val masterCountString = Transformations.map(mastersLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val masterCount = Transformations.map(mastersLive) { worker ->
        worker.count
    }
    val masterBuySingleButtonText = Transformations.map(mastersLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val masterBuySingleButtonEnabled = Transformations.map(mastersLive) { worker ->
        worker.cost < _widget.count
    }
    val masterBuySetButtonText = Transformations.map(mastersLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }
    val masterTotalSpeedString = Transformations.map(mastersLive) { worker ->
        totalSpeedString(worker)
    }

    val adeptCountString = Transformations.map(adeptsLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }
    val adeptCount = Transformations.map(adeptsLive) { worker ->
        worker.count
    }
    val adeptBuySingleButtonText = Transformations.map(adeptsLive) { worker ->
        application.resources.getString(R.string.novice_buy_single_button)
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }
    val adeptBuySingleButtonEnabled = Transformations.map(adeptsLive) { worker ->
        worker.cost < _widget.count
    }
    val adeptBuySetButtonText = Transformations.map(adeptsLive) { worker ->
        application.resources.getString(R.string.novice_buy_set_button)
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }
    val adeptTotalSpeedString = Transformations.map(adeptsLive) { worker ->
        totalSpeedString(worker)
    }

    fun totalSpeedString(worker: Worker): String {
        return WidgetHelpers.formatNumbers(worker.totalSpeed()) + "/s"
    }

    val widgetCount = Transformations.map(widgetsLive) { widget -> widget.count }
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
            when (worker.type) {
                "novice" -> novices = worker
                "amateur" -> amateurs = worker
                "apprentice" -> apprentices = worker
                "journeyman" -> journeymen = worker
                "master" -> masters = worker
                "adept" -> adepts = worker
            }
        }
    }

    fun updateWidgets(widget: Widget) {
        widgets = widget
    }

    fun purchaseAmateurs(count: Int) {
        val worker = amateurs
        if (purchaseWorkers(worker, count)) {
            // Assign to cause set.
            amateurs = worker
        }
    }

    fun purchaseNovices(count: Int) {
        val worker = novices
        if (purchaseWorkers(worker, count)) {
            novices = worker
        }
    }

    fun purchaseApprentices(count: Int) {
        val worker = apprentices
        if (purchaseWorkers(worker, count)) {
            apprentices = worker
        }
    }

    fun purchaseMasters(count: Int) {
        val worker = masters
        if (purchaseWorkers(worker, count)) {
            masters = worker
        }
    }

    fun purchaseJourneymen(count: Int) {
        val worker = journeymen
        if (purchaseWorkers(worker, count)) {
            journeymen = worker
        }
    }

    fun purchaseAdepts(count: Int) {
        val worker = adepts
        if (purchaseWorkers(worker, count)) {
            adepts = worker
        }
    }

    fun purchaseWorkers(worker: Worker, count: Int): Boolean {
        var cost = worker.costToCount(count)
        if (cost < widgets.count) {
            val widgetCopy = widgets
            widgetCopy.count -= cost
            worker.purchase(count)
            widgets = widgetCopy
            return true
        }
        return false
    }

    fun loadState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            setWidgetCount(savedInstanceState.getDouble(widgetCountSavedInstanceKey))
            setAmateursCount(savedInstanceState.getDouble(amateurCountSavedInstanceKey))
            setNoviceCount(savedInstanceState.getDouble(noviceCountSavedInstanceKey))
            setApprenticesCount(savedInstanceState.getDouble(apprenticeCountSavedInstanceKey))
            setJourneymenCount(savedInstanceState.getDouble(journeymenCountSavedInstanceKey))
            setMastersCount(savedInstanceState.getDouble(masterCountSavedInstanceKey))
            setadeptsCount(savedInstanceState.getDouble(adeptCountSavedInstanceKey))
        }
    }

    fun onSaveInstanceState(outState: Bundle) {

        outState.putDouble(widgetCountSavedInstanceKey, widgets.count)
        outState.putDouble(amateurCountSavedInstanceKey, amateurs.count)
        outState.putDouble(apprenticeCountSavedInstanceKey, apprentices.count)
        outState.putDouble(noviceCountSavedInstanceKey, novices.count)
        outState.putDouble(masterCountSavedInstanceKey, masters.count)
        outState.putDouble(journeymenCountSavedInstanceKey, journeymen.count)
        outState.putDouble(adeptCountSavedInstanceKey, adepts.count)
    }

    @ExperimentalTime
    fun produce() {
        widgets = addProducedWidgets(
            widgets,
            amateurs.produce(lastRun.Since.inMilliseconds)
        )
        amateurs = addProducedWorker(
            amateurs,
            novices.produce(lastRun.Since.inMilliseconds)
        )
        novices = addProducedWorker(
            novices,
            apprentices.produce(lastRun.Since.inMilliseconds)
        )
        apprentices = addProducedWorker(
            apprentices,
            journeymen.produce(lastRun.Since.inMilliseconds)
        )
        journeymen = addProducedWorker(
            journeymen,
            masters.produce(lastRun.Since.inMilliseconds)
        )
        masters = addProducedWorker(
            masters,
            adepts.produce(lastRun.Since.inMilliseconds)
        )
        adepts = addProducedWorker(adepts, 0.0)

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
                purchaseAmateurs(amateurs.countToSet())
            }
            R.id.amateurBuySingle -> {
                purchaseAmateurs(1)
            }
            R.id.noviceBuySingle -> {
                purchaseNovices(1)
            }
            R.id.noviceBuySet -> {
                purchaseNovices(novices.countToSet())
            }
            R.id.apprenticeBuySingle -> {
                purchaseApprentices(1)
            }
            R.id.apprenticeBuySet -> {
                purchaseApprentices(apprentices.countToSet())
            }
            R.id.journeymanBuySingle -> purchaseJourneymen(1)
            R.id.journeymanBuySet -> purchaseJourneymen(journeymen.countToSet())
            R.id.masterBuySingle -> {
                purchaseMasters(1)
            }
            R.id.masterBuySet -> {
                purchaseMasters(masters.countToSet())
            }
            R.id.adeptBuySingle -> purchaseAdepts(1)
            R.id.adeptBuySet -> purchaseAdepts(adepts.countToSet())
            R.id.resetButton -> resetAll()
        }
    }

    private fun resetAll() {
        novices = resetWorker(_novices)
        amateurs = resetWorker(_amateurs)
        apprentices = resetWorker(_apprentices)
        journeymen = resetWorker(_journeymen)
        masters = resetWorker(_masters)
        adepts = resetWorker(_adepts)
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
        workerRepository.insertAll(listOf(_novices, _amateurs, _apprentices, _journeymen, _masters))
    }
}
