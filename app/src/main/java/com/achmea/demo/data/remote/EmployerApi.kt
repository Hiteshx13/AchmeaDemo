package com.achmea.demo.data.remote

import com.achmea.demo.data.remote.dto.EmployerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployerApi {
    @GET("/CBAEmployerservice.svc/rest/employers")
    suspend fun getEmployers(
        @Path("filter") filter: String,
        @Path("maxRows") maxRow: Int
    ): List<EmployerDto>
}