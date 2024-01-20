package com.achmea.demo.data.repository

import com.achmea.demo.data.local.EmployerDao
import com.achmea.demo.data.local.toEmployer
import com.achmea.demo.data.remote.EmployerApi
import com.achmea.demo.data.remote.dto.toEmployer
import com.achmea.demo.data.remote.dto.toEmployerEntity
import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.repository.EmployerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class EmployerRepositoryImpl(
    private val apiService: EmployerApi,
    private val employerDao: EmployerDao,
) : EmployerRepository {
    override suspend fun getEmployerData(
        filter: String,
        maxRows: Int
    ): List<Employer> {

        return withContext(Dispatchers.IO) {

            val currentTimestamp = System.currentTimeMillis()
            val oneWeekAgo = currentTimestamp - TimeUnit.DAYS.toMillis(7)

            // Delete data older than 1 week
            employerDao.deleteOldData(oneWeekAgo)

            //retrieve data from local storage if already cached
            val cachedEmployers = employerDao.getEmployersByFilter(filter)
            if (cachedEmployers.isNotEmpty()) {
                return@withContext cachedEmployers.map { it.toEmployer() }
            }

            // If not in cache, fetch from the network
            val newEmployers = apiService.getEmployers(filter, maxRows)

            // Save to local cache
            val entities = newEmployers.map { it.toEmployerEntity() }
            employerDao.insertAllEmployers(entities)

            return@withContext newEmployers.map { it.toEmployer() }
        }
    }
}