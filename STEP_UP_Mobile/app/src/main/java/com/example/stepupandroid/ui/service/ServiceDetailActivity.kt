package com.example.stepupandroid.ui.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.adapter.ViewPagerAdapter
import com.example.stepupandroid.databinding.ActivityServiceDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.viewmodel.ServiceDetailViewModel

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var viewModel: ServiceDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ServiceDetailViewModel(this)
        initViewModel()

        val serviceId = intent.getIntExtra("serviceId", 0)

        viewModel.getServiceDetail(serviceId)

    }

    private fun initViewModel() {
        viewModel.serviceDetailResultState.observe(this) { result ->
            if (result.result.attachments.isNotEmpty()) {
                val imageUrls = result.result.attachments.values.toList()
                val adapter = ViewPagerAdapter(imageUrls)
                binding.imageViewPager.adapter = adapter
            }
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

    }
}