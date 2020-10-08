package com.galamdring.idledev

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerRepository

class WorkerManager(private var worker: Worker) {

    val WorkerBuySingleButtonFormatString = "Buy 1\n%s"
    val WorkerBuySetButtonFormatString = "Buy %d\n%s"

    val WorkerLive = MutableLiveData<Worker>(worker)

    var count: Double
        get() = worker.count
        set(value) {
            worker.count = value
            WorkerLive.postValue(worker)
        }

    val countToSet: Int
        get() = worker.countToSet()

    var purchasedCount: Int
        get() = worker.purchased
        set(count) {
            worker.purchased = count
            WorkerLive.postValue(worker)
        }

    var cost: Double
        get() = worker.cost
        set(cost: Double) {
            worker.cost = cost
            WorkerLive.postValue(worker)
        }

    fun priceToCount(count: Int): Double {
        return worker.priceToCount(count)
    }

    fun purchase(count: Int): Boolean {
        if (count > worker.countToSet()) {
            return false
        }
        worker.purchase(count)
        return true
    }

    fun produce(inMilliseconds: Double): Double {
        return worker.produce(inMilliseconds)
    }

    val CountString = Transformations.map(WorkerLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }

    val TotalSpeedString = Transformations.map(WorkerLive) { worker ->
        WidgetHelpers.formatNumbers(worker.totalSpeed()) + "/s"
    }

    val BuySingleButtonText = Transformations.map(WorkerLive) { worker ->
        WorkerBuySingleButtonFormatString
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }

    val BuySetButtonText = Transformations.map(WorkerLive) { worker ->
        WorkerBuySetButtonFormatString
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }

    fun setWorker(newWorker: Worker) {
        worker = newWorker
        WorkerLive.postValue(worker)
    }

    companion object Factory {
        const val NoviceString = "novice"
        private lateinit var novice: WorkerManager

        const val AmateurString = "amateur"
        private lateinit var amateur: WorkerManager

        const val ApprenticeString = "apprentice"
        private lateinit var apprentice: WorkerManager

        val JourneymanString = "journeyman"
        private lateinit var journeyman: WorkerManager

        val MasterString = "master"
        private lateinit var master: WorkerManager

        val AdeptString = "adept"
        private lateinit var adept: WorkerManager

        fun getWorker(type: String, workerRepository: WorkerRepository): WorkerManager {
            when (type) {
                NoviceString -> {
                    if (this::novice.isInitialized) {
                        return novice
                    }
                    val worker = workerRepository.novices ?: workerRepository.defaultNovice
                    novice = WorkerManager(worker)
                    return novice
                }
                AmateurString -> {
                    if (this::amateur.isInitialized) {
                        return amateur
                    }
                    val worker = workerRepository.amateurs ?: workerRepository.defaultAmateur
                    amateur = WorkerManager(worker)
                    return amateur
                }
                ApprenticeString -> {
                    if (this::apprentice.isInitialized) {
                        return apprentice
                    }
                    val worker = workerRepository.apprentices ?: workerRepository.defaultApprentice
                    apprentice = WorkerManager(worker)
                    return apprentice
                }
                JourneymanString -> {
                    if (this::journeyman.isInitialized) {
                        return journeyman
                    }
                    val worker = workerRepository.journeymen ?: workerRepository.defaultJourneyman
                    journeyman = WorkerManager(worker)
                    return journeyman
                }
                MasterString -> {
                    if (this::master.isInitialized) {
                        return master
                    }
                    val worker = workerRepository.masters ?: workerRepository.defaultMaster
                    master = WorkerManager(worker)
                    return master
                }
                AdeptString -> {
                    if (this::adept.isInitialized) {
                        return adept
                    }
                    val worker = workerRepository.adepts ?: workerRepository.defaultAdept
                    adept = WorkerManager(worker)
                    return adept
                }
                else -> {
                    return WorkerManager(workerRepository.defaultAmateur)
                }
            }
        }

        fun saveAll(repository: WorkerRepository) {
            repository.insertAll(
                listOf(
                    novice.worker,
                    amateur.worker,
                    apprentice.worker,
                    journeyman.worker,
                    master.worker
                )
            )
        }

        fun setWorker(worker: Worker) {
            when (worker.type) {
                NoviceString -> novice = WorkerManager(worker)
                AmateurString -> amateur = WorkerManager(worker)
                ApprenticeString -> apprentice = WorkerManager(worker)
                JourneymanString -> journeyman = WorkerManager(worker)
                MasterString -> master = WorkerManager(worker)
                AdeptString -> adept = WorkerManager(worker)
            }
        }

        fun resetWorkers(repository: WorkerRepository) {
            amateur.setWorker(repository.defaultAmateur)
            novice.setWorker(repository.defaultNovice)
            apprentice.setWorker(repository.defaultApprentice)
            journeyman.setWorker(repository.defaultJourneyman)
            master.setWorker(repository.defaultMaster)
            adept.setWorker(repository.defaultAdept)
        }
    }
}