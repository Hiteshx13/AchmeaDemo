package com.achmea.demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployerDao {

    @Query("SELECT * FROM employerentity WHERE name= :employer")
    fun getEmployersByFilter(employer: String): List<EmployerEntity>

    @Query("DELETE FROM employerentity")
    fun deleteAllEmployers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEmployers(list: List<EmployerEntity>)

    @Query("DELETE FROM employerentity WHERE timestamp < :oneWeekAgo")
    fun deleteOldData(oneWeekAgo: Long)
}