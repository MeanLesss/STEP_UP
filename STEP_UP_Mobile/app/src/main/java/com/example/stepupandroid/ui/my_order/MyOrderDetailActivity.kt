package com.example.stepupandroid.ui.my_order

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityMyOrderDetailBinding
import com.example.stepupandroid.databinding.ActivityMyWorkDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.OrderDetailViewModel

class MyOrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyOrderDetailBinding
    private lateinit var viewModel: OrderDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailBinding.inflate(layoutInflater)
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