package com.achmea.demo.domain.repository

import com.achmea.demo.domain.model.Employer

interface EmployerRepository {
    suspend fun getEmployerData(filter: String, maxRows: Int?): List<Employer>
    suspend fun getAllCachedEmployers(): List<Employer>
    suspend fun deleteExpiredDataFromLocalDB()
}