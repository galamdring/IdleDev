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
        set(value) {
            worker!!.count = value
        }

    var cost: Double
        get() = worker!!.cost
        set(value) {
            worker!!.cost = value
        }


    fun updateFromDB(worker: Worker) {
        this.count = worker.count
        this.cost = worker.cost
    }
}