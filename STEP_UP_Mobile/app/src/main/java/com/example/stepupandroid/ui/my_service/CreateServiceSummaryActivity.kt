package com.example.stepupandroid.ui.my_service

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.databinding.ActivityCreateServiceSummaryBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.model.param.CreateServiceParam
import com.example.stepupandroid.viewmodel.CreateServiceViewModel

class CreateServiceSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateServiceSummaryBinding
    private lateinit var viewModel: CreateServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateServiceSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val body: CreateServiceParam = intent.getParcelableExtra("body")!!
        initData(body)

        viewModel = CreateServiceViewModel(this)

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.createServiceBtn.setOnClickListener {
            viewModel.createService(body)
        }

        viewModel.createServiceResultState.observe(this) {
            startActivity(Intent(this, CreateServiceSuccessActivity::class.java))
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }

    }
    @SuppressLint("SetTextI18n")
    private fun initData(body: CreateServiceParam) {
        binding.title.text = resources.getString(R.string.title_text) + " " + body.title
        binding.description.text = resources.getString(R.string.description_text) + " " + body.description
        binding.price.text = resources.getString(R.string.price_text) + " " + body.price
        binding.serviceType.text = resources.getString(R.string.service_type_text) + " " + body.service_type
        binding.startDate.text = resources.getString(R.string.start_date_text) + " " + body.start_date
        binding.endDate.text = resources.getString(R.string.end_date_text) + " " + body.end_date

        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.attachmentRecyclerView.adapter = AttachmentAdapter(body.attachments.toMutableList())
    }
}