package com.achmea.demo.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.achmea.demo.data.repository.FakeEmployerRepositoryImpl
import com.achmea.demo.domain.repository.EmployerRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEmployerUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EmployerRepository

    @Before
    fun setUp() {
        repository = FakeEmployerRepositoryImpl()
    }

    @Test
    fun getEmployers() {
    }

    @Test
    fun getAllCachedEmployers() {
    }
}