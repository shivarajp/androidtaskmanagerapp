package com.masai.taskmanagerapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.masai.taskmanagerapp.adapter.OnTaskItemClicked
import com.masai.taskmanagerapp.adapter.TasksAdapter
import com.masai.taskmanagerapp.database.DatabaseHandler
import com.masai.taskmanagerapp.models.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnTaskItemClicked {

    lateinit var taskAdapter:TasksAdapter
    private val tasksList = mutableListOf<Task>()
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            dbHandler.insertTask("Buy Milk", "Buy fresh")
            updateUI()
        }

        tasksList.addAll(dbHandler.getAllTasks())

        taskAdapter = TasksAdapter(this, tasksList, this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = taskAdapter
    }

    override fun onEditClicked(task: Task) {
        val newTitle = "New title"
        val newDesc = "New Desc"

        task.tite = newTitle
        task.desc = newDesc

        dbHandler.editTask(task)
        updateUI()
    }

    override fun onDeleteClicked(task: Task) {
        dbHandler.deleteTask(task)
        updateUI()
    }

    fun updateUI(){
        val latestTasks = dbHandler.getAllTasks()
        tasksList.clear()
        tasksList.addAll(latestTasks)
        taskAdapter.notifyDataSetChanged()
    }

}