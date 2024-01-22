package com.achmea.demo.domain.repository

import com.achmea.demo.common.DataState
import com.achmea.demo.domain.model.Employer

interface EmployerRepository {
    suspend fun getEmployerData(filter: String, maxRows: Int?): DataState<List<Employer>>
    suspend fun getAllCachedEmployers(): List<Employer>
    suspend fun deleteExpiredDataFromLocalDB()
}