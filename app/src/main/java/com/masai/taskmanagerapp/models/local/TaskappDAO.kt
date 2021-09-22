package com.masai.taskmanagerapp.models.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskappDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTasks(tasks: ArrayList<Task>)

    @Query("select * from tasks")
    fun getTasks(): LiveData<List<Task>>

    @Update
    fun updateTask(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("delete from tasks")
    fun deleteAll()

}