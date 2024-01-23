package com.example.stepupandroid.ui.my_work

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.adapter.ResourceAdapter
import com.example.stepupandroid.databinding.ActivityMyWorkDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.param.SubmitWorkParam
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.CancelDialog
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.dialog.SelectFileDialog
import com.example.stepupandroid.viewmodel.WorkDetailViewModel

class MyWorkDetailActivity : AppCompatActivity(), SelectFileDialog.OnFileSelectedListener,
    CancelDialog.OnCancelListener {
    private lateinit var binding: ActivityMyWorkDetailBinding
    private lateinit var viewModel: WorkDetailViewModel

    private var isShow = true
    private var isUpdate = false

    private var workId = 0
    private var serviceId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWorkDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = WorkDetailViewModel(this)
        initViewModel()

        workId = intent.getIntExtra("workId", 0)

        viewModel.getWorkDetail(workId)

        binding.backBtn.setOnClickListener {
            if (isUpdate) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("from", Constants.MyWork)
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

        binding.declineBtn.setOnClickListener {
            val body = HashMap<String, Boolean>()
            body["isAccept"] = false
            viewModel.acceptWork(body, workId)
        }

        binding.acceptBtn.setOnClickListener {
            val body = HashMap<String, Boolean>()
            body["isAccept"] = true
            viewModel.acceptWork(body, workId)
        }

        binding.cancelBtn.setOnClickListener {
            val dialog =
                CancelDialog("You will get 5 credit score deducted for cancel before expected end date!")
            dialog.setOnCancelListener(this)
            dialog.show(supportFragmentManager, "CancelDialog")
        }

        binding.completeBtn.setOnClickListener {
            val dialog = SelectFileDialog()
            dialog.setOnFileSelectedListener(this)
            dialog.show(supportFragmentManager, "SelectFileDialog")
        }
    }

    override fun onFileSelected(attachment: Attachment) {
        val body = SubmitWorkParam(
            order_id = workId.toString(),
            service_id = serviceId.toString(),
            attachments = listOf(attachment)
        )
        viewModel.submitWork(body)
    }

    override fun onCancel(description: String) {
        val body = HashMap<String, String>()
        body["order_id"] = workId.toString()
        body["service_id"] = serviceId.toString()
        body["cancel_desc"] = description
        viewModel.cancelWork(body)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.workDetailResultState.observe(this) { result ->
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


            binding.pendingStatusButton.visibility = View.GONE
            binding.inProgressStatusButton.visibility = View.GONE
            if (result.result.stringStatus == Constants.Pending) {
                binding.pendingStatusButton.visibility = View.VISIBLE
            } else if (result.result.stringStatus == Constants.InProgress) {
                binding.inProgressStatusButton.visibility = View.VISIBLE
            }

        }

        viewModel.acceptWorkResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getWorkDetail(workId)
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.submitWorkResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getWorkDetail(workId)
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.cancelWorkResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getWorkDetail(workId)
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