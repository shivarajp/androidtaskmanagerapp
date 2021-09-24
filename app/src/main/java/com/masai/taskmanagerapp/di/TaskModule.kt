package com.masai.taskmanagerapp.di

import android.content.Context
import androidx.room.Room
import com.masai.taskmanagerapp.BuildConfig
import com.masai.taskmanagerapp.models.local.TaskRoomDatabase
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.Network
import com.masai.taskmanagerapp.models.remote.TasksAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {
    /**
     * Provides ApiServices client for Retrofit
     */
    @Singleton
    @Provides
    fun provideTasksAPIService(): TasksAPI {
        return Retrofit.Builder()
            .baseUrl("http://13.232.169.202:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TasksAPI::class.java)
    }


    /**
     * Provides app AppDatabase
     */
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): TaskRoomDatabase =
        Room.databaseBuilder(context, TaskRoomDatabase::class.java, "tasks-db")
            .fallbackToDestructiveMigration().build()


    /**
     * Provides TasksDao an object to access NewsArticles table from Database
     */
    @Singleton
    @Provides
    fun provideTasksDao(db: TaskRoomDatabase): TaskappDAO = db.getTaskDAO()

    }