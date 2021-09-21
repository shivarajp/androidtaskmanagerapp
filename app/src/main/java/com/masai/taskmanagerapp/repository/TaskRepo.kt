package com.masai.taskmanagerapp.repository

import androidx.lifecycle.LiveData
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.Network
import com.masai.taskmanagerapp.models.remote.Resource
import com.masai.taskmanagerapp.models.remote.ResponseHandler
import com.masai.taskmanagerapp.models.remote.TasksAPI
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.responses.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TaskRepo(private val taskDAO: TaskappDAO) {

    private val api: TasksAPI = Network.getRetrofit().create(TasksAPI::class.java)
    private val responseHandler = ResponseHandler()

    suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponse>{
        return try {
            val response = api.login(loginRequestModel)
            responseHandler.handleSuccess(response)
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    fun addTaskToRoom(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.addTask(task)
        }
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return taskDAO.getTasks()
    }

    fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.delete(task)
        }
    }

}