package com.galamdring.idledev.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: Widget)

    @Update
    fun update(widget: Widget)

    @Query("SELECT * from widget WHERE widgetId = :key")
    fun get(key: Long): Widget?

    @Query("Select * from widget order by widgetId desc limit 1")
    fun getFirst(): LiveData<Widget>

    @Query("select Exists(select * from widget)")
    fun hasItems(): Boolean

    @Query("Select * from widget order by widgetId desc limit 1")
    fun getWidget(): Widget
}
