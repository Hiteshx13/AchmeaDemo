package com.achmea.demo.data.repository

import com.achmea.demo.MyApplication
import com.achmea.demo.R
import com.achmea.demo.common.Constants
import com.achmea.demo.common.DataState
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
        maxRows: Int?
    ): DataState<List<Employer>> {
        return withContext(Dispatchers.IO) {

            //retrieve data from local storage if already cached
            val cachedEmployers = employerDao.getEmployersByFilterAndMaxRow(filter, maxRows)

            if (cachedEmployers.isNotEmpty() && maxRows == null ||
                cachedEmployers.isNotEmpty() && maxRows!! <= cachedEmployers.size
            ) {
                return@withContext DataState.Success(cachedEmployers.map { it.toEmployer() })
            } else {
                // If not in cache, fetch from the network
                if (Constants.isInternetAvailable(MyApplication.appContext)) {
                    val newEmployers = apiService.getEmployers(filter, maxRows)

                    if (newEmployers.isNotEmpty()) {
                        val currentTimestamp = System.currentTimeMillis()
                        val entities = newEmployers.map {
                            it.timestamp = currentTimestamp
                            it.toEmployerEntity()
                        }
                        employerDao.insertAllEmployers(entities)
                        val latestCachedEmployers =
                            employerDao.getEmployersByFilterAndMaxRow(filter, maxRows)
                        return@withContext DataState.Success(latestCachedEmployers.map { it.toEmployer() })
                    } else {
                        return@withContext DataState.Success(listOf<Employer>())
                    }
                } else {
                    return@withContext DataState.Error(
                        message = MyApplication.appContext.getString(
                            R.string.no_internet_connection
                        )
                    )
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