package com.galamdring.idledev.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galamdring.idledev.WidgetHelpers

@Entity(tableName = "workers")
data class Worker(
    @PrimaryKey(autoGenerate = true)
    var workerId: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "speed")
    var speed: Double,
    @ColumnInfo(name = "count")
    var count: Double,
    @ColumnInfo(name = "purchased")
    var purchased: Int,
    @ColumnInfo(name = "setBonus")
    var setBonus: Double,
    @ColumnInfo(name = "setSize")
    var setSize: Int,
    @ColumnInfo(name = "cost")
    var cost: Double,
    @ColumnInfo(name = "costIncrease")
    var costIncrease: Double,
    @ColumnInfo(name = "interval")
    var interval: Int

) {

    val countString: String
        get() = WidgetHelpers.formatNumbers(count)

    fun totalSpeed(): Double {
        // get the flat speed rate for count
        var flatSpeed = this.count * this.speed
        // bonus based on how many set groups have been purchased
        var bonusCount = this.purchased / this.setSize
        if (bonusCount == 0) return flatSpeed
        return flatSpeed * bonusCount * (this.setBonus)
    }

    fun produce(msSinceUpdate: Double): Double {
        if (this.count == 0.0) {
            return 0.0
        }
        return (this.totalSpeed() * msSinceUpdate / this.interval)
    }

    fun countToSet(): Int {
        return this.setSize - this.purchased % this.setSize
    }

    fun priceToSet(): Double {
        return this.priceToCount(this.countToSet())
    }

    fun purchase(count: Int) {
        if (count <= 0 || count > this.setSize) {
            return
        }
        this.count += count
        this.purchased += count

        // Increase cost for the next one
        this.cost = increasePrice(count, this.cost, this.costIncrease)
    }

    fun increasePrice(numToSet: Int, startingCost: Double, interval: Double): Double {
        var newPrice = startingCost
        for (i in 0 until numToSet) {
            newPrice = newPrice.times(interval)
        }
        return newPrice
    }

    fun purchaseNextSet() {
        this.purchase(this.countToSet())
    }

    fun priceToCount(count: Int): Double {
        var priceToCount = 0.0
        var currentPrice = this.cost
        for (i in 0 until count) {
            priceToCount += currentPrice
            currentPrice *= this.costIncrease
        }
        return priceToCount
    }

}
