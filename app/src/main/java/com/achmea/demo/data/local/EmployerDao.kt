package com.achmea.demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployerDao {

    @Query("SELECT * FROM employerentity WHERE name LIKE '%' ||:employer || '%' ORDER BY name ASC  LIMIT CASE WHEN :maxRows IS NULL THEN -1 ELSE :maxRows END")
    fun getEmployersByFilterAndMaxRow(employer: String, maxRows: Int?): List<EmployerEntity>

    @Query("SELECT * FROM employerentity ORDER BY name ASC")
    fun getAllEmployers(): List<EmployerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmployers(list: List<EmployerEntity>)

    @Query("DELETE FROM employerentity WHERE timestamp < :oneWeekAgo")
    suspend fun deleteOldData(oneWeekAgo: Long)
}