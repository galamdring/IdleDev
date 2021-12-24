package com.galamdring.idledev.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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

    @Query("update workers set name= :worker.name, type = :worker.type, " +
            "speed = :worker.speed, count= :worker.count, " +
            "purchased= :worker.purchased, setBonus= :worker.setBonus, " +
            "setSize= :worker.setSize, cost = :worker.cost, " +
            "costIncrease = :worker.costIncrease, interval= :worker.interval" +
            " where type like :worker.type")
    suspend fun insertWorker(worker: Worker)
}
