package com.galamdring.idledev.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widget: Widget)

    @Update
    suspend fun update(widget: Widget)

    @Query("SELECT * from widget WHERE widgetId = :key")
    suspend fun get(key: Long): Widget?

    @Query("Select * from widget order by widgetId desc limit 1")
    fun getFirst(): LiveData<Widget>

    @Query("select Exists(select * from widget)")
    suspend fun hasItems(): Boolean

    @Query("Select * from widget order by widgetId desc limit 1")
    suspend fun getWidget(): Widget
}
