package com.masai.taskmanagerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.remote.Resource
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.responses.LoginResponse
import com.masai.taskmanagerapp.repository.TaskRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(val repo: TaskRepo) : ViewModel() {

    fun userLogin(loginRequestModel: LoginRequestModel): LiveData<Resource<LoginResponse>> {

        val liveData =

            liveData(Dispatchers.IO) {
            val result = repo.login(loginRequestModel)
            emit(result)
        }

        return liveData
    }

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