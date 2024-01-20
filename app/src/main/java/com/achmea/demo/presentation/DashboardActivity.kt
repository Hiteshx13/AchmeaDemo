package com.achmea.demo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.achmea.demo.R
import com.achmea.demo.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        binding.fab.setOnClickListener {
            showFilterDialog(this,  true, object : DialogOnClick {
                override fun onApply(filter: String, maxRow: Int) {

                }
            })
        }

    }
}