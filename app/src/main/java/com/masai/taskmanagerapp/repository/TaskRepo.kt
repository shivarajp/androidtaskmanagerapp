package com.masai.taskmanagerapp.repository

import androidx.lifecycle.LiveData
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.Network
import com.masai.taskmanagerapp.models.remote.Resource
import com.masai.taskmanagerapp.models.remote.ResponseHandler
import com.masai.taskmanagerapp.models.remote.TasksAPI
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.responses.CreatetaskRequestModel
import com.masai.taskmanagerapp.models.remote.responses.GetTasksResponseModel
import com.masai.taskmanagerapp.models.remote.responses.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TaskRepo(private val taskDAO: TaskappDAO) {

    private val api: TasksAPI = Network.getRetrofit().create(TasksAPI::class.java)
    private val responseHandler = ResponseHandler()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MGE0YmI3OTAzMjdlN2MwNmE2MTk1ODYiLCJpYXQiOjE2MzIxMzg2ODR9.cTxpYQrTfvramIOSPih6b1hJO_x1G-V2GmaRnTYSjU0"

    suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponse>{
        return try {
            val response = api.login(loginRequestModel)
            responseHandler.handleSuccess(response)
        }catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    fun getRemoteTasks(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getTasksFromAPI(token)
            saveToDB(response)
        }
    }

    fun createTask(createtaskRequestModel: CreatetaskRequestModel){
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.createTask(token, createtaskRequestModel)
            val newtask = Task(response.title, response.description)
            taskDAO.addTask(newtask)
        }
    }

    private fun saveToDB(response: GetTasksResponseModel) {

        val listOFTasks = ArrayList<Task>()
        response.forEach {
            val newTask = Task(it.title, it.description)
            listOFTasks.add(newTask)
        }
        taskDAO.deleteAll()
        taskDAO.addTasks(listOFTasks)
    }


    private fun saveTasksToDb(response: GetTasksResponseModel) {
        val tasks = ArrayList<Task>()
        response.forEach {
            val task = Task(it.title, it.description)
            tasks.add(task)
        }
        taskDAO.addTasks(tasks)
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