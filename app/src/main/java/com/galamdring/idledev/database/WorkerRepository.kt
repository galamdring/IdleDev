package com.galamdring.idledev.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WorkerRepository(private val application: Application) {
    companion object {
        private var INSTANCE: WorkerRepository? = null

        fun getInstance(application: Application): WorkerRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = WorkerRepository(application)
                }
                INSTANCE = instance
                return instance
            }
        }
    }

    val defaultAmateur = Worker(
        0, "widgets", "amateur", 1.2, 0.0, 0, 1.2,
        10, 100.0, 1.1, 1000
    )

    val defaultNovice = Worker(
        0, "widgets", "novice", 1.0, 0.0, 0, 1.2,
        10, 1000.0, 1.2, 1000
    )

    val defaultApprentice = Worker(
        0, "widgets", "apprentice", .8, 0.0, 0, 1.2,
        10, 20000.0, 1.3, 1000
    )

    val defaultJourneyman = Worker(
        0, "widgets", "journeyman", .7, 0.0, 0, 1.2,
        10, 1000000.0, 1.4, 1000
    )

    val defaultMaster = Worker(
        0, "widgets", "master", .6, 0.0, 0, 1.2,
        10, 1000000000.0, 1.5, 1000
    )

    val defaultAdept = Worker(
        0, "widgets", "adept", .5, 0.0, 0, 1.2,
        10, 100000000000.0, 1.6, 1000
    )

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val workerDao = WorkerDatabase.getInstance(application).workerDao

    val allWorkers: LiveData<List<Worker>>
        get() = workerDao.getAllWorkers()

    val novices: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("novice")
        }

    val amateurs: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("amateur")
        }

    val apprentices: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("apprentice")
        }

    val journeymen: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("journeyman")
        }

    val masters: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("master")
        }

    val adepts: Worker?
        get() = runBlocking {
            return@runBlocking workerDao.getWorker("adept")
        }

    fun insert(worker: Worker) {
        GlobalScope.launch { workerDao.insert(worker) }
    }

    fun insertAll(workers: List<Worker>) {
        GlobalScope.launch { workerDao.insertAll(workers) }
    }

    fun hasType(type: String): Boolean = runBlocking {
        return@runBlocking workerDao.hasType(type)
    }

    val defaultValues = mapOf(
        "amateur" to defaultAmateur,
        "novice" to defaultNovice,
        "apprentice" to defaultApprentice,
        "journeyman" to defaultJourneyman,
        "master" to defaultMaster,
        "adept" to defaultAdept
    )

    fun initializeDB() {
        MainScope().launch {
            for (worker in defaultValues) {
                if (!hasType(worker.key)) {
                    insert(worker.value)
                }
            }
        }
    }
}
