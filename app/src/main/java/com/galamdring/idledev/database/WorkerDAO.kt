package com.galamdring.idledev.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(worker: Worker)

    @Update
    suspend fun update(worker: Worker)

    @Query("SELECT * from workers WHERE workerId = :key")
    suspend fun get(key: Long): Worker?

    @Delete
    suspend fun clear(workers: List<Worker>)

    @Query("SELECT * FROM workers where type like :type")
    suspend fun getWorker(type: String): Worker?

    @Query("SELECT * FROM workers where type like :type")
    fun getWorkerLiveData(type: String): LiveData<Worker>

    @Query("Select * from workers Order by workerId Desc")
    fun getAllWorkers(): LiveData<List<Worker>>

    @Query("SELECT EXISTS(SELECT * FROM workers where type like :type)")
    suspend fun hasType(type: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workers: List<Worker>)
}
