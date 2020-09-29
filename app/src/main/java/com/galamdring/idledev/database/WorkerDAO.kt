package com.galamdring.idledev.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(worker: Worker)

    @Update
    fun update(worker: Worker)

    @Query("SELECT * from workers WHERE workerId = :key")
    fun get(key: Long): Worker?

    @Query("DELETE FROM workers")
    fun clear()

    @Query("SELECT * FROM workers where type like :type")
    fun getWorker(type: String): Worker

    @Query("SELECT * FROM workers where type like :type")
    fun getWorkerLiveData(type: String): LiveData<Worker>

    @Query("Select * from workers Order by workerId Desc")
    fun getAllWorkers(): LiveData<List<Worker>>

    @Query("SELECT EXISTS(SELECT * FROM workers where type like :type)")
    fun hasType(type: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(workers: List<Worker>)
}
