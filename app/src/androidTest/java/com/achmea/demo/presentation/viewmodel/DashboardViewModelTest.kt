package com.achmea.demo.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.achmea.demo.common.DataState
import com.achmea.demo.data.repository.FakeEmployerRepositoryImpl
import com.achmea.demo.domain.use_case.GetEmployerUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class DashboardViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel
    private lateinit var useCaseTest: GetEmployerUseCase

    @Before
    fun setUp() {
        useCaseTest = GetEmployerUseCase(FakeEmployerRepositoryImpl())
        viewModel = DashboardViewModel(useCaseTest)
    }


    @Test
    fun getEmployers() {
        viewModel.getEmployers("Ach", null)
        val value = viewModel.employerData
        assertThat((value.value as DataState.Success).data).isNotEmpty()

    }

    @Test
    fun getAllCachedEmployers() {
    }
}