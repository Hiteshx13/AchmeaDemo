package com.achmea.demo.data.repository

import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.repository.EmployerRepository

class FakeEmployerRepositoryImpl : EmployerRepository {
    val list= mutableListOf<Employer>()
    override suspend fun getEmployerData(filter: String, maxRows: Int?): List<Employer> {
        return list
    }

    override suspend fun getAllCachedEmployers(): List<Employer> {
        return list
    }

    override suspend fun deleteExpiredDataFromLocalDB() {

    }


}