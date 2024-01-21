package com.achmea.demo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achmea.demo.MyApplication
import com.achmea.demo.R
import com.achmea.demo.common.DataState
import com.achmea.demo.databinding.ActivityDashboardBinding
import com.achmea.demo.domain.model.Employer
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

        // get cached data from local database
        viewModel.getAllCachedEmployers()
    }

    private fun initObserver() {
        viewModel.employerData.observe(this) { data ->

            when (data) {
                is DataState.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is DataState.Error -> {
                    updateViewVisibility(false)
                    binding.progressbar.isVisible = false
                    binding.tvMessage.text = data.message
                }

                is DataState.Success -> {
                    binding.progressbar.isVisible = false
                    if (data.data.isNullOrEmpty()) {
                        updateViewVisibility(false)
                        binding.tvMessage.text = resources.getString(R.string.no_data_found)
                    } else {
                        updateViewVisibility(true)
                        showEmployerList(data.data)
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        binding.fab.setOnClickListener {
            showFilterDialog()
        }

        binding.btnGetEmployer.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        showFilterDialog(this, true, object : DialogOnClick {
            override fun onApply(filter: String, maxRow: Int?) {
                viewModel.getEmployers(filter, maxRow)
            }
        })
    }

    private fun showEmployerList(list: List<Employer>) {
        val recyclerView: RecyclerView = findViewById(R.id.rvEmployers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EmployerAdapter(list)
        recyclerView.adapter = adapter
    }

    private fun updateViewVisibility(isDataAvailable: Boolean) {
        binding.rvEmployers.isVisible = isDataAvailable
        binding.tvMessage.isVisible = !isDataAvailable
        binding.btnGetEmployer.isVisible = !isDataAvailable
        binding.fab.isVisible = isDataAvailable
    }
}