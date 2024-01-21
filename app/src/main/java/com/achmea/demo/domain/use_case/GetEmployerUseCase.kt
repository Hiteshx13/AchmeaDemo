package com.achmea.demo.domain.use_case

import com.achmea.demo.common.NetworkResult
import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.repository.EmployerRepository

class GetEmployerUseCase(
    private val repository: EmployerRepository
) {
    suspend fun getEmployers(filter: String, maxRows: Int): NetworkResult<List<Employer>> {
        NetworkResult.Loading<Employer>()
        return try {
            val result = repository.getEmployerData(filter, maxRows)
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An error occurred")
        }
    }
    suspend fun getAllCachedEmployers(): NetworkResult<List<Employer>> {
        NetworkResult.Loading<Employer>()
        return try {
            val result = repository.getAllCachedEmployers()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An error occurred")
        }
    }
}