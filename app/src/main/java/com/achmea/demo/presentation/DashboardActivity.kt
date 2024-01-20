package com.achmea.demo.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.achmea.demo.MyApplication
import com.achmea.demo.R
import com.achmea.demo.common.NetworkResult
import com.achmea.demo.databinding.ActivityDashboardBinding
import com.achmea.demo.domain.use_case.GetEmployerUseCase

class DashboardActivity : AppCompatActivity() {

    lateinit var viewModel: DashboardViewModel
    private val binding: ActivityDashboardBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_dashboard
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory {

            val authRepository = MyApplication.appModule.employerRepository
            val mainUseCase = GetEmployerUseCase(authRepository)
            DashboardViewModel(mainUseCase)
        })[DashboardViewModel::class.java]

        initObserver()
        initClickListener()
    }

    private fun initObserver() {
        viewModel.employerData.observe(this) { data ->
            when (data) {
                is NetworkResult.Loading -> {
                    binding.tvText.text = "Loading..."
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, data.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.tvText.text = data.errorMessage
                }

                is NetworkResult.Success -> {
                    binding.tvText.text = "Success..."
                }
            }
        }

    }

    private fun initClickListener() {
        binding.fab.setOnClickListener {
            showFilterDialog(this, true, object : DialogOnClick {
                override fun onApply(filter: String, maxRow: Int) {
                    viewModel.getEmployers(filter, maxRow)
                }
            })
        }
    }
}