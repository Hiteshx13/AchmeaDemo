package com.achmea.demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EmployerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val employerDao: EmployerDao
}