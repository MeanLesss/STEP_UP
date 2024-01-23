package com.example.stepupandroid.ui.my_order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.ResourceAdapter
import com.example.stepupandroid.databinding.ActivityMyOrderDetailBinding
import com.example.stepupandroid.databinding.ActivityMyWorkDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.CancelDialog
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.OrderDetailViewModel

class MyOrderDetailActivity : AppCompatActivity(),
    CancelDialog.OnCancelListener {
    private lateinit var binding: ActivityMyOrderDetailBinding
    private lateinit var viewModel: OrderDetailViewModel

    private var isShow = true
    private var isUpdate = false

    private var orderId = 0
    private var serviceId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = OrderDetailViewModel(this)
        initViewModel()

        orderId = intent.getIntExtra("orderId", 0)

        viewModel.getOrderDetail(orderId)

        binding.backBtn.setOnClickListener {
            if (isUpdate) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("from", Constants.MyOrder)
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }

        binding.hideBtn.setOnClickListener {
            if (isShow) {
                isShow = false
                binding.keyLayout.visibility = View.GONE
                binding.valueLayout.visibility = View.GONE
                binding.summary.visibility = View.VISIBLE

                binding.hideBtn.icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.icon_up, null)
            } else {
                isShow = true
                binding.keyLayout.visibility = View.VISIBLE
                binding.valueLayout.visibility = View.VISIBLE
                binding.summary.visibility = View.GONE

                binding.hideBtn.icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.icon_down, null)
            }
        }

        binding.cancelBtn.setOnClickListener {
            val dialog =
                CancelDialog("If you cancel, you will only receive a refund of 50% of your payment.")
            dialog.setOnCancelListener(this)
            dialog.show(supportFragmentManager, "CancelDialog")
        }
    }

    override fun onCancel(description: String) {
        val body = HashMap<String, String>()
        body["order_id"] = orderId.toString()
        body["service_id"] = serviceId.toString()
        body["cancel_desc"] = description
        viewModel.cancelOrder(body)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.orderDetailResultState.observe(this) { result ->
            serviceId = result.result.service_id

            if (result.result.order_attachments.isNotEmpty()) {
                val resourceList: MutableList<String> = mutableListOf()
                result.result.order_attachments.keys.forEach {
                    resourceList.add(it)
                }
                binding.resourceRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.resourceRecyclerView.adapter = ResourceAdapter(resourceList)
            } else {
                binding.resource.visibility = View.GONE
            }

            binding.buttonLayout.visibility = View.VISIBLE

            binding.orderTitle.text = result.result.order_title
            binding.orderDescription.text = result.result.order_description
            binding.status.text = result.result.stringStatus
            binding.contactName.text = result.result.contact.name
            binding.contactEmail.text = result.result.contact.email
            binding.startDate.text = Util.convertDate(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "dd-MMM-yyyy",
                result.result.expected_start_date
            )
            binding.endDate.text = Util.convertDate(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "dd-MMM-yyyy",
                result.result.expected_end_date
            )
            binding.price.text = result.result.service.price
            binding.serviceType.text = result.result.service_order
            if (!result.result.accepted_at.isNullOrEmpty()) {
                binding.acceptDate.text = Util.convertDate(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "dd-MMM-yyyy",
                    result.result.accepted_at
                )
            } else {
                binding.acceptDate.text = "Not Yet Accepted"
            }

            binding.cancelBtnLayout.visibility = View.GONE
            if (result.result.stringStatus == Constants.Pending || result.result.stringStatus == Constants.InProgress) {
                binding.cancelBtnLayout.visibility = View.VISIBLE
            }

        }

        viewModel.confirmOrderResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getOrderDetail(orderId)
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.cancelOrderResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getOrderDetail(orderId)
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        binding.backBtn.performClick()
    }
}