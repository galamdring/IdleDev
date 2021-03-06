package com.galamdring.idledev.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widget")
data class Widget(
    @PrimaryKey(autoGenerate = true)
    var widgetId: Long = 0L,
    @ColumnInfo(name = "count")
    var count: Double
) {

    fun purchase(count: Double): Boolean {
        if (this.count >= count) {
            this.count -= count
            return true
        }
        return false
    }
}
