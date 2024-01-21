package com.example.stepupandroid.ui.my_work

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stepupandroid.databinding.ActivityMyWorkDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.HomeActivity
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

        val orderId = intent.getIntExtra("workId", 0)

        viewModel.getOrderDetail(orderId)

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("from", Constants.MyWork)
            startActivity(intent)
            finish()
        }

        binding.declineBtn.setOnClickListener {
            val body = HashMap<String, Boolean>()
            body["isAccept"] = false
            viewModel.acceptOrder(body, orderId)
        }

        binding.acceptBtn.setOnClickListener {
            val body = HashMap<String, Boolean>()
            body["isAccept"] = true
            viewModel.acceptOrder(body, orderId)
        }

        binding.cancelBtn.setOnClickListener {
//            val body = HashMap<String, Boolean>()
//            body["isAccept"] = false
//            viewModel.acceptOrder(body, orderId)
        }

        binding.completeBtn.setOnClickListener {
//            val body = HashMap<String, Boolean>()
//            body["isAccept"] = true
//            viewModel.acceptOrder(body, orderId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.orderDetailResultState.observe(this) { result ->
            if (result.result.order_attachments.isNotEmpty()) {
                //order attachment recycler view
            }

            binding.orderTitle.text = result.result.order_title
            binding.orderDescription.text = result.result.order_description

            if (result.result.stringStatus == Constants.Pending) {
                binding.pendingStatusButton.visibility = View.VISIBLE
            } else if (result.result.stringStatus == Constants.InProgress) {
                binding.inProgressStatusButton.visibility = View.VISIBLE
            }

        }

        viewModel.acceptOrderResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)
            customDialog.onDismissListener = {
                recreate()
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

    }
}