package com.masai.taskmanagerapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.masai.taskmanagerapp.models.Task

class DatabaseHandler(val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION ) {

    companion object{
        val DB_NAME = "tasksdb"
        val DB_VERSION = 1

        val TABLE_NAME = "tasks"
        val ID = "id"
        val TITLE = "title"
        val DESC = "desc"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE_QUERY = "CREATE TABLE " +
                "$TABLE_NAME(" +
                "$ID INTEGER PRIMARY KEY, " +
                "$TITLE TEXT, " +
                "$DESC TEXT)"

        db?.execSQL(CREATE_TABLE_QUERY)
    }

    fun insertTask(title: String, desc: String){
        val db = writableDatabase

        val values = ContentValues()
        values.put(TITLE, title)
        values.put(DESC, desc)

        val idValue = db.insert(TABLE_NAME, null, values)

        if (idValue.toInt() == -1){
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Successfully inserted the row", Toast.LENGTH_LONG).show()
        }
    }

    fun getAllTasks() : MutableList<Task> {
        val taskList = mutableListOf<Task>()

        val db = readableDatabase
        val query = "select * from $TABLE_NAME"

        val cursor = db?.rawQuery(query, null)

        if(cursor != null && cursor.count > 0){
            cursor.moveToFirst()

            do {
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DESC))

                val task = Task()
                task.id = id
                task.tite = title
                task.desc = desc

                taskList.add(task)

            } while (cursor.moveToNext())
        }
        return taskList
    }

    fun editTask(task: Task){
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, task.tite)
        contentValues.put(DESC, task.desc)

        val result = db.update(TABLE_NAME, contentValues, "id = ${task.id}" , null)

        if (result == 1){
            Toast.makeText(context, "Task Updated", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_LONG).show()
        }
    }


    fun deleteTask(task: Task){
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "id=${task.id}", null)
        if (result == 1){
            Toast.makeText(context, "Task Deleted", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show()
        }
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {



    }
}