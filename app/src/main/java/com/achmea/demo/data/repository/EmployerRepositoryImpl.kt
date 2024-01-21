package com.achmea.demo.data.repository

import com.achmea.demo.data.local.EmployerDao
import com.achmea.demo.data.local.toEmployer
import com.achmea.demo.data.remote.EmployerApi
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

            //retrieve data from local storage if already cached
            val cachedEmployers = if (maxRows > 1) {
                employerDao.getEmployersByFilterAndMaxRow(filter, maxRows)
            } else {
                employerDao.getEmployersByFilter(filter)
            }

            if (cachedEmployers.isNotEmpty() && maxRows<=cachedEmployers.size) {
                return@withContext cachedEmployers.map { it.toEmployer() }
            } else {
                // If not in cache, fetch from the network
                val newEmployers = apiService.getEmployers(filter, maxRows)

                if (newEmployers.isNotEmpty()) {
                    val currentTimestamp = System.currentTimeMillis()
                    val entities = newEmployers.map {
                        it.timestamp = currentTimestamp
                        it.toEmployerEntity()
                    }

                    employerDao.insertAllEmployers(entities)
                    val latestCachedEmployers = employerDao.getEmployersByFilter(filter)
                    return@withContext latestCachedEmployers.map { it.toEmployer() }
                } else {
                    return@withContext listOf<Employer>()
                }
            }
        }
    }

    override suspend fun getAllCachedEmployers(): List<Employer> {
        return withContext(Dispatchers.IO) {
            val latestCachedEmployers = employerDao.getAllEmployers()
            return@withContext latestCachedEmployers.map { it.toEmployer() }
        }
    }

    override suspend fun deleteExpiredDataFromLocalDB() {
        val currentTimestamp = System.currentTimeMillis()
        val oneWeekAgo = currentTimestamp - TimeUnit.DAYS.toMillis(7)

        // Delete data older than 1 week
        employerDao.deleteOldData(oneWeekAgo)

    }
}