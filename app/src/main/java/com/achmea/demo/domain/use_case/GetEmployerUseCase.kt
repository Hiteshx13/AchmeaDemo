package com.achmea.demo.domain.use_case

import com.achmea.demo.common.DataState
import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.repository.EmployerRepository

class GetEmployerUseCase(
    private val repository: EmployerRepository
) {
    suspend fun getEmployers(filter: String, maxRows: Int?): DataState<List<Employer>> {
        return try {
            val result = repository.getEmployerData(filter, maxRows)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getAllCachedEmployers(): DataState<List<Employer>> {
        return try {
            val result = repository.getAllCachedEmployers()
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e.message ?: "An error occurred")
        }
    }
}