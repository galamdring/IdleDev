package com.galamdring.idledev

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerDAO
import com.galamdring.idledev.database.WorkerDatabase
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkerDatabaseTest {

    private lateinit var workerDao: WorkerDAO
    private lateinit var db: WorkerDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.databaseBuilder(context, WorkerDatabase::class.java, "testDb.db")
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
                .build()
        workerDao = db.workerDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWorker() {
        val novice = Worker(0,"widgets", "novice", 1.1, 0.0, 0, 1.2,
            10, 1000.0, 1.1, 1000)
        workerDao.insert(novice)
        val workers = workerDao.getAllWorkers()
        val workersData = workers.value
        val worker = workerDao.getWorker("novice")
        val workerData = worker
        assertNotNull(workerData)
        assertEquals("novice", workerData.type)
    }
}