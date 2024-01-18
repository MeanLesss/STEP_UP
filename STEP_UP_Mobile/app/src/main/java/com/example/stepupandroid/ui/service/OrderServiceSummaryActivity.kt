package com.example.stepupandroid.ui.service

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.databinding.ActivityOrderServiceSummaryBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.param.OrderServiceParam
import com.example.stepupandroid.model.response.OrderSummary
import com.example.stepupandroid.ui.SuccessActivity
import com.example.stepupandroid.viewmodel.OrderServiceViewModel

class OrderServiceSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderServiceSummaryBinding
    private lateinit var viewModel: OrderServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderServiceSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val body: OrderServiceParam = intent.getParcelableExtra("body")!!
        val summary: OrderSummary = intent.getParcelableExtra("summary")!!
        initData(body, summary)

        viewModel = OrderServiceViewModel(this)
        initViewModel()

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.orderServiceBtn.setOnClickListener {
            body.expected_start_date = Util.convertDate("MMM dd, yyyy", "yyyy-MM-dd", body.expected_start_date)
            body.expected_end_date = Util.convertDate("MMM dd, yyyy", "yyyy-MM-dd", body.expected_end_date)
            viewModel.orderService(body)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initData(body: OrderServiceParam, summary: OrderSummary) {
        binding.title.text = resources.getString(R.string.title_text) + " " + body.order_title
        binding.description.text = resources.getString(R.string.description_text) + " " + body.order_description
        binding.startDate.text = resources.getString(R.string.start_date_text) + " " + body.expected_start_date
        binding.endDate.text = resources.getString(R.string.end_date_text) + " " + body.expected_end_date

        binding.taxNote.text = resources.getString(R.string.note_text) + " " + summary.tax
        binding.price.text = Util.formatCurrency(summary.price)
        binding.tax.text = Util.formatCurrency(summary.taxAmount)
        binding.totalPrice.text = Util.formatCurrency(summary.totalPrice)

        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.attachmentRecyclerView.adapter = AttachmentAdapter(body.attachments.toMutableList())
    }

    private fun initViewModel(){
        viewModel.orderServiceResultState.observe(this){
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.order_has_been_placed))
            startActivity(intent)
        }
        viewModel.errorResultState.observe(this){
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }
    }
}