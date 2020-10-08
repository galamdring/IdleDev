package com.galamdring.idledev.database

import org.junit.Assert.assertEquals
import org.junit.Test

class WorkerTest {
    private val testWorker = Worker(
        0, "widgets", "novice", 1.0, 0.0, 0, 1.2,
        10, 1000.0, 1.2, 1000
    )

    @Test
    fun getCountString() {
        val myWorker = testWorker
        assertEquals(myWorker.countString, "    0")
    }

    @Test
    fun totalSpeed() {
        assertEquals(testWorker.totalSpeed(), 0.0, 1.0)
    }

    @Test
    fun produce() {
        val myworker = testWorker.copy()
        myworker.purchaseNextSet()
        assertEquals("worker should produce", 12.0, myworker.produce(1000.0), 1.0)
    }

    @Test
    fun countToSet() {
        val myworker = testWorker.copy()
        myworker.purchase(4)
        assertEquals(6, myworker.countToSet())
    }

    @Test
    fun priceToSet() {
        val myworker = testWorker.copy()
        myworker.purchase(5)
        assertEquals(myworker.cost * 5, myworker.priceToSet(), 1.0)
    }

    @Test
    fun purchase() {
        val worker = testWorker.copy()
        worker.purchase(5)
        assertEquals(5, worker.purchased)
        assertEquals(5.0, worker.count, 1.0)
    }

    @Test
    fun increasePrice() {
        val worker = testWorker.copy()
        val cost = worker.cost
        assertEquals(cost * worker.costIncrease, worker.increasePrice(1, cost, worker.costIncrease), 1.0)
    }

    @Test
    fun purchaseNextSet() {
        val worker = testWorker.copy()
        worker.purchaseNextSet()
        assertEquals(10, worker.purchased)
        assertEquals(10.0, worker.count, 1.0)
    }

    @Test
    fun priceToCount() {
        assertEquals(testWorker.cost, testWorker.priceToCount(1), 1.0)
        assertEquals((testWorker.cost + testWorker.cost), testWorker.priceToCount(2), 1.0)
    }
}
