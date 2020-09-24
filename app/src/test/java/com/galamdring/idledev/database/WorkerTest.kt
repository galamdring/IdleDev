package com.galamdring.idledev.database

import org.junit.Test

import com.google.common.truth.Truth.assertThat

class WorkerTest {

    var TestWorker = Worker(
        0, "widgets", "amateur", 1.2, 0.0, 0, 1.2,
        10, 100.0, 1.1, 1000
    )

    @Test
    fun getCountString() {
        assertThat(TestWorker.countString).isEqualTo("    0")
    }

    @Test
    fun totalSpeed() {
    }

    @Test
    fun produce() {
    }

    @Test
    fun countToSet() {
    }

    @Test
    fun priceToSet() {
    }

    @Test
    fun purchase() {
    }

    @Test
    fun increasePrice() {
    }

    @Test
    fun purchaseNextSet() {
    }

    @Test
    fun priceToCount() {
    }

    @Test
    fun costToCount() {
    }
}