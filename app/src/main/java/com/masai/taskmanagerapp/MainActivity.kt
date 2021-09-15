package com.masai.taskmanagerapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.masai.taskmanagerapp.adapter.OnTaskItemClicked
import com.masai.taskmanagerapp.adapter.TasksAdapter
import com.masai.taskmanagerapp.models.Task
import com.masai.taskmanagerapp.models.TaskRoomDatabase
import com.masai.taskmanagerapp.models.TaskappDAO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnTaskItemClicked {

    lateinit var taskAdapter: TasksAdapter
    private val tasksList = mutableListOf<Task>()

    lateinit var roomDb: TaskRoomDatabase
    lateinit var taskDao: TaskappDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        roomDb = TaskRoomDatabase.getDatabaseObject(this)
        taskDao = roomDb.getTaskDAO()
        /**

        @entity
        Entity: Annotated class that describes a database table when working with Room.

        SQLite database: On-device storage.
        The Room persistence library creates and maintains this database for you.

        @dao
        DAO: Data access object.
        A mapping of SQL queries to functions.
        When you use a DAO, you call the methods, and Room takes care of the rest.

        @database
        Room database: Simplifies database work and serves as an access
        point to the underlying SQLite database (hides SQLiteOpenHelper).
        The Room database uses the DAO to issue queries to the SQLite database.

         */


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val newTask = Task("Dummy ttile", "Dummy desc")
            CoroutineScope(Dispatchers.IO).launch {
                taskDao.addTask(newTask)
            }
        }


        taskAdapter = TasksAdapter(this, tasksList, this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = taskAdapter

        taskDao.getTasks().observe(this@MainActivity, Observer {
            val tasks = it
            tasksList.clear()
            tasksList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
        })

    }

    override fun onEditClicked(task: Task) {
        val newTitle = "New title"
        val newDesc = "New Desc"

        task.title = newTitle
        task.desc = newDesc

        CoroutineScope(Dispatchers.IO).launch {
            taskDao.updateTask(task)
        }
    }

    override fun onDeleteClicked(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.delete(task)
        }
    }

}