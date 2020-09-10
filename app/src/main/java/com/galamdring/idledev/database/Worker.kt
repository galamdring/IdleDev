package com.galamdring.idledev.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galamdring.idledev.WidgetsManager

@Entity(tableName="workers")
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
)