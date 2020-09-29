package com.galamdring.idledev.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Worker::class, Widget::class], version = 2, exportSchema = false)
abstract class WorkerDatabase : RoomDatabase() {

    abstract val workerDao: WorkerDAO
    abstract val widgetDao: WidgetDao

    companion object {

        @Volatile
        private var INSTANCE: WorkerDatabase? = null

        fun getInstance(context: Context): WorkerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkerDatabase::class.java,
                        "workers_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
