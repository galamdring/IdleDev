package com.galamdring.idledev

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerRepository

class WorkerManager(private var worker: Worker) {

    private val workerBuySingleButtonFormatString = "Buy 1\n%s"
    private val workerBuySetButtonFormatString = "Buy %d\n%s"

    val workerLive = MutableLiveData<Worker>(worker)

    var count: Double
        get() = worker.count
        set(value) {
            worker.count = value
            workerLive.postValue(worker)
        }

    val countToSet: Int
        get() = worker.countToSet()

    var purchasedCount: Int
        get() = worker.purchased
        set(count) {
            worker.purchased = count
            workerLive.postValue(worker)
        }

    var cost: Double
        get() = worker.cost
        set(cost: Double) {
            worker.cost = cost
            workerLive.postValue(worker)
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

    val countString = Transformations.map(workerLive) { worker ->
        WidgetHelpers.formatNumbers(worker.count)
    }

    val totalSpeedString = Transformations.map(workerLive) { worker ->
        WidgetHelpers.formatNumbers(worker.totalSpeed()) + "/s"
    }

    val buySingleButtonText = Transformations.map(workerLive) { worker ->
        workerBuySingleButtonFormatString
            .format(WidgetHelpers.formatNumbers(worker.cost))
    }

    val buySetButtonText = Transformations.map(workerLive) { worker ->
        workerBuySetButtonFormatString
            .format(worker.countToSet(), WidgetHelpers.formatNumbers(worker.priceToSet()))
    }

    fun setWorker(newWorker: Worker) {
        worker = newWorker
        workerLive.postValue(worker)
    }

    fun loadState(stateCount: Double, stateCost: Double, statePurchasedCount: Int) {
        if (stateCount > 0) count = stateCount
        if (stateCost > 0) cost = stateCost
        if (statePurchasedCount > 0) purchasedCount = statePurchasedCount
    }

    companion object Factory {
        const val NoviceString = "novice"
        private lateinit var novice: WorkerManager

        const val AmateurString = "amateur"
        private lateinit var amateur: WorkerManager

        const val ApprenticeString = "apprentice"
        private lateinit var apprentice: WorkerManager

        const val JourneymanString = "journeyman"
        private lateinit var journeyman: WorkerManager

        const val MasterString = "master"
        private lateinit var master: WorkerManager

        const val AdeptString = "adept"
        private lateinit var adept: WorkerManager

        fun getWorker(type: String, workerRepository: WorkerRepository): WorkerManager {
            var worker = WorkerManager(workerRepository.defaultAmateur)
            when (type) {
                NoviceString -> {
                    worker = getNovice(workerRepository)
                }
                AmateurString -> {
                    worker = getAmateur(workerRepository)
                }
                ApprenticeString -> {
                    worker = getApprentice(workerRepository)
                }
                JourneymanString -> {
                    worker = getJourneyman(workerRepository)
                }
                MasterString -> {
                    worker = getMaster(workerRepository)
                }
                AdeptString -> {
                    worker = getAdept(workerRepository)
                }
            }
            return worker
        }

        private fun getNovice(workerRepository: WorkerRepository): WorkerManager {
            if (!this::novice.isInitialized) {
                novice = WorkerManager(workerRepository.novices ?: workerRepository.defaultNovice)
            }
            return novice
        }

        private fun getAdept(
            workerRepository: WorkerRepository,
        ): WorkerManager {
            if (!this::adept.isInitialized) {
                adept = WorkerManager(workerRepository.adepts ?: workerRepository.defaultAdept)
            }
            return adept
        }

        private fun getMaster(
            workerRepository: WorkerRepository,
        ): WorkerManager {
            if (!this::master.isInitialized) {
                master = WorkerManager(workerRepository.masters ?: workerRepository.defaultMaster)
            }
            return master
        }

        private fun getJourneyman(
            workerRepository: WorkerRepository,
        ): WorkerManager {
            if (!this::journeyman.isInitialized) {
                journeyman =
                    WorkerManager(workerRepository.journeymen ?: workerRepository.defaultJourneyman)
            }
            return journeyman
        }

        private fun getApprentice(
            workerRepository: WorkerRepository,
        ): WorkerManager {
            if (!this::apprentice.isInitialized) {
                apprentice = WorkerManager(
                    workerRepository.apprentices ?: workerRepository.defaultApprentice
                )
            }
            return apprentice
        }

        private fun getAmateur(
            workerRepository: WorkerRepository,
        ): WorkerManager {
            if (!this::amateur.isInitialized) {
                amateur =
                    WorkerManager(workerRepository.amateurs ?: workerRepository.defaultAmateur)
            }
            return amateur
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
