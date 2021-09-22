package com.masai.taskmanagerapp.models.remote.responses

data class CreatetaskRequestModel(
    val description: String,
    val status: Int,
    val title: String
)