package com.achmea.demo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmea.demo.common.NetworkResult
import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.use_case.GetEmployerUseCase
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getEmployerUseCase: GetEmployerUseCase
) : ViewModel() {

    private var _employerData = MutableLiveData<NetworkResult<List<Employer>>>()
    val employerData: LiveData<NetworkResult<List<Employer>>> = _employerData

    fun getEmployers(filter: String, maxRows: Int) {
        viewModelScope.launch {
            val result = getEmployerUseCase.getEmployers(filter, maxRows)
            _employerData.value = result
        }
    }

    fun getAllCachedEmployers() {
        viewModelScope.launch {
            val result = getEmployerUseCase.getAllCachedEmployers()
            _employerData.value = result
        }
    }
}