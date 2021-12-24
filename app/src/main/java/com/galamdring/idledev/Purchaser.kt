package com.galamdring.idledev

import com.galamdring.idledev.database.Widget
import kotlinx.coroutines.channels.Channel

object Purchaser {
    private lateinit var channel: Channel<PurchaserContract>

    suspend fun purchaseWorker(worker: WorkerManager, widget: Widget, count: Int) {
        channel.send(PurchaserContract(worker, widget, count))
    }

    suspend fun listenForPurchases(queueSize: Int) {
        channel = Channel(queueSize)
        while (true) {
            val contract = channel.receive()
            val cost = contract.worker.priceToCount(contract.count)
            if (contract.widget.count > cost) {
                contract.widget.count -= cost
                contract.worker.purchase(contract.count)
            }
        }
    }
}

class PurchaserContract(var worker: WorkerManager, var widget: Widget, var count: Int)
