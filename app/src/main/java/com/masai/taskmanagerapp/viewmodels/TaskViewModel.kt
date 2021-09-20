package com.masai.taskmanagerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.masai.taskmanagerapp.models.Task
import com.masai.taskmanagerapp.repository.TaskRepo

class TaskViewModel(val repo: TaskRepo) : ViewModel() {

    fun addTask(task: Task){
        repo.addTaskToRoom(task)
    }

    fun getTasks(): LiveData<List<Task>> {
        return repo.getAllTasks()
    }

    fun updateTask(task: Task){
        repo.updateTask(task)
    }

    fun delete(task: Task){
        repo.deleteTask(task)
    }

}