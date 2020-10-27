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

}
