package com.achmea.demo.data.remote

import com.achmea.demo.data.remote.dto.EmployerDto
import retrofit2.http.GET
import retrofit2.http.Query

interface EmployerApi {
    @GET("CBAEmployerservice.svc/rest/employers")
    suspend fun getEmployers(
        @Query("filter") filter: String,
        @Query("maxRows") maxRows: Int
    ): List<EmployerDto>
}