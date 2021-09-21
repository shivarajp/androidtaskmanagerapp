package com.masai.taskmanagerapp.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 1)
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun getTaskDAO(): TaskappDAO

    companion object {

        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabaseObject(context: Context): TaskRoomDatabase {

            if (INSTANCE == null) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_db"
                )

                builder.fallbackToDestructiveMigration()

                INSTANCE = builder.build()
                return INSTANCE!!
            } else {
                return INSTANCE!!
            }
        }

    }


}