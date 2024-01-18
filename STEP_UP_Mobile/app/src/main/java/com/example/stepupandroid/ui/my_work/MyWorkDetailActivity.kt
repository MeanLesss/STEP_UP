package com.example.stepupandroid.ui.my_work

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stepupandroid.databinding.ActivityMyWorkDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.OrderDetailViewModel

class MyWorkDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyWorkDetailBinding
    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWorkDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = OrderDetailViewModel(this)
        initViewModel()

        val orderId = intent.getIntExtra("orderId", 0)

        viewModel.getOrderDetail(orderId)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.orderDetailResultState.observe(this) { result ->
            if (result.result.order_attachments.isNotEmpty()) {
                //order attachment recycler view
            }

            binding.orderTitle.text = result.result.order_title
            binding.orderDescription.text = result.result.order_description

        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

    }
}