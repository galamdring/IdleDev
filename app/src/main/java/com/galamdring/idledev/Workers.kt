package com.galamdring.idledev

import android.app.Application
import android.content.Context
import android.content.OperationApplicationException
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.galamdring.idledev.database.Worker
import com.galamdring.idledev.database.WorkerDAO
import com.galamdring.idledev.database.WorkerDatabase


class Workers(
    var worker: Worker,
    var widgetsManager: WidgetsManager
)  {

    var count: Double
    get() {
        if (worker==null){
            return 0.0
        }
        return worker!!.count
    }
    set(value) { worker!!.count = value }

    var cost: Double
        get() = worker!!.cost
        set(value) { worker!!.cost = value }

    fun countToSet() : Int {
        return this.worker!!.setSize - this.worker!!.purchased%this.worker!!.setSize
    }

    fun priceToSet() : Double {
        return this.priceToCount(this.countToSet())
    }

    fun purchase(count: Int): Boolean {
        if(count <= 0 || count > this.worker!!.setSize){
            return false;
        }
        // Deduct cost
        if(widgetsManager.chargeForType(this.worker!!.name, this.priceToCount(count))){//We could afford it
            this.worker!!.count += count
            this.worker!!.purchased+=count
            // Increase cost for the next one, using Math.ceil() to round up
            this.worker!!.cost = increasePrice(count,this.worker!!.cost,this.worker!!.costIncrease)
            if(this.countToSet()==this.worker!!.setSize){ // A set was just completed
                Toast.makeText(widgetsManager.getApplication(), "Buying " + this.worker!!.setSize +
                        " gets you a bonus, currently " + helpers.formatNumbers(
                    (this.worker!!.purchased/this.worker!!.setSize)*this.worker!!.setBonus),
                    Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return false
    }

    fun increasePrice(numToSet: Int, startingCost: Double, interval: Double) : Double{
        var newPrice = startingCost
        for(i in 0 until numToSet){
            newPrice = newPrice.times(interval);
        }
        return newPrice
    }

    fun purchaseNextSet() : Boolean {
        return this.purchase(this.countToSet());
    }

    fun priceToCount(count: Int) : Double {
        var priceToCount = 0.0
        var currentPrice = this.worker!!.cost
        for (i in 0 until count){
            priceToCount+= currentPrice
            currentPrice *= this.worker!!.costIncrease
        }
        return priceToCount;
    }

    fun produce(msSinceUpdate: Double): Double{
        if (this.worker!!.count == 0.0){
            return 0.0
        }
        return (this.totalSpeed() * msSinceUpdate / this.worker!!.interval)
    }

    fun totalSpeed(): Double {
        // get the flat speed rate for count
        var flatSpeed = this.count * this.worker!!.speed;
        //bonus based on how many set groups have been purchased
        var bonusCount = this.worker!!.purchased/this.worker!!.setSize;
        if (bonusCount == 0) return flatSpeed;
        return flatSpeed * bonusCount * (this.worker!!.setBonus );
    }
}