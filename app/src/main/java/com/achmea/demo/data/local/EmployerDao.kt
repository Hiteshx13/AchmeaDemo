package com.achmea.demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployerDao {

    @Query("SELECT * FROM employerentity WHERE name LIKE '%' ||:employer || '%'")
    fun getEmployersByFilter(employer: String): List<EmployerEntity>

    @Query("SELECT * FROM employerentity")
    fun getAll(): List<EmployerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmployers(list: List<EmployerEntity>)

    @Query("DELETE FROM employerentity WHERE timestamp < :oneWeekAgo")
    suspend fun deleteOldData(oneWeekAgo: Long)
}