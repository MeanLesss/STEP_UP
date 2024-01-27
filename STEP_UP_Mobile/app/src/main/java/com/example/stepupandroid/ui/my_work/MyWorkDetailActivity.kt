package com.example.stepupandroid.ui.my_work

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
import com.example.stepupandroid.ui.dialog.ConfirmDialog
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.dialog.SelectFileDialog
import com.example.stepupandroid.viewmodel.WorkDetailViewModel
import java.io.File

class MyWorkDetailActivity : AppCompatActivity(), SelectFileDialog.OnFileSelectedListener,
    CancelDialog.OnCancelListener {
    private lateinit var binding: ActivityMyWorkDetailBinding
    private lateinit var viewModel: WorkDetailViewModel

    private var isShow = true
    private var isUpdate = false

    private var workId = 0
    private var serviceId = 0
    private var fileName = ""
    private var fileUrl = ""

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
                finishAffinity()
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
            val dialog = ConfirmDialog("Are you sure you want to decline?") {
                val body = HashMap<String, Boolean>()
                body["isAccept"] = false
                viewModel.acceptWork(body, workId)
            }
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }

        binding.acceptBtn.setOnClickListener {
            val dialog = ConfirmDialog("Are you sure you want to accept?") {
                val body = HashMap<String, Boolean>()
                body["isAccept"] = true
                viewModel.acceptWork(body, workId)
            }
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }

        binding.cancelBtn.setOnClickListener {
            val dialog =
                CancelDialog("If you cancel before the expected end date, your credit score will be reduced by 5 points.")
            dialog.setOnCancelListener(this)
            dialog.show(supportFragmentManager, "CancelDialog")
        }

        binding.completeBtn.setOnClickListener {
            val dialog = SelectFileDialog()
            dialog.setOnFileSelectedListener(this)
            dialog.show(supportFragmentManager, "SelectFileDialog")
        }

        binding.downloadWork.setOnClickListener {
            checkPermissionAndDownload()
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

            if (result.result.completed_attachments.isNotEmpty()) {
                fileUrl = result.result.completed_attachments.values.firstOrNull().toString()
                fileName = result.result.completed_attachments.keys.firstOrNull().toString()

                binding.downloadWork.visibility = View.VISIBLE
            } else {
                binding.downloadWork.visibility = View.GONE
            }

            binding.buttonLayout.visibility = View.VISIBLE

            binding.orderTitle.text = result.result.order_title
            binding.orderDescription.text = result.result.order_description
            binding.status.text = result.result.stringStatus
            binding.contactName.text = result.result.contact.name
            binding.contactEmail.text = result.result.contact.email
            binding.startDate.text = Util.convertDate(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MMM-yyyy", result.result.expected_start_date
            )
            binding.endDate.text = Util.convertDate(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MMM-yyyy", result.result.expected_end_date
            )
            binding.price.text = result.result.service.price
            binding.serviceType.text = result.result.service_order
            if (!result.result.accepted_at.isNullOrEmpty()) {
                binding.acceptDate.text = Util.convertDate(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MMM-yyyy", result.result.accepted_at
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

    private val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1

    private fun checkPermissionAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped Storage is being used, no need for WRITE_EXTERNAL_STORAGE permission
            download()
        } else {
            // For older versions, continue using the WRITE_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE
                )
            } else {
                download()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, proceed with your operation
                    download()
                } else {
                    // Permission denied, handle the case
                    val customDialog = CustomDialog(
                        "Permission denied.",
                        "Please allow the permission to download.",
                        Constants.Warning
                    )

                    customDialog.show(supportFragmentManager, "CustomDialog")
                }
                return
            }
        }
    }

    private fun download() {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileUrl
        )
        if (file.exists()) {
            file.delete()
        }

        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

        val request = DownloadManager.Request(Uri.parse(fileUrl))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDescription("Downloading work...")
        request.setTitle("Download")
        request.setTitle(fileName)
        if (mimeType != null) {
            request.setMimeType(mimeType)
        }

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        val query = DownloadManager.Query().setFilterById(downloadId)
        showDownloadProgressDialog()


        val handler = Handler(Looper.getMainLooper())
        val maxIdleDuration = 30000 // Maximum idle duration in milliseconds
        var lastProgressUpdateTime = System.currentTimeMillis()

        object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - lastProgressUpdateTime
                val cursor = manager.query(query)

                if (cursor.moveToFirst()) {
                    val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                    when (cursor.getInt(statusColumnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            dismissProgressDialog()
                            val customDialog =
                                CustomDialog("", "Download Completed.", Constants.Success)

                            customDialog.show(supportFragmentManager, "CustomDialog")
                        }

                        DownloadManager.STATUS_FAILED -> {
                            dismissProgressDialog()
                            val customDialog =
                                CustomDialog("", "File not found.", Constants.Warning)

                            customDialog.show(supportFragmentManager, "CustomDialog")
                        }

                        else -> {
                            if (elapsedTime > maxIdleDuration) {
                                dismissProgressDialog()
                                manager.remove(downloadId)
                                // Handle download timeout/idle here
                            } else {
                                val downloadedColumnIndex =
                                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                val totalSizeColumnIndex =
                                    cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                                if (downloadedColumnIndex >= 0 && totalSizeColumnIndex >= 0) {
                                    val bytesDownloaded = cursor.getInt(downloadedColumnIndex)
                                    val bytesTotal = cursor.getInt(totalSizeColumnIndex)
                                    val progress =
                                        if (bytesTotal > 0) (bytesDownloaded * 100L / bytesTotal).toInt() else 0
                                    updateProgressDialog(progress)
                                    lastProgressUpdateTime = System.currentTimeMillis()
                                }
                                handler.postDelayed(this, 1000)
                            }
                        }
                    }
                }
                cursor.close()
            }
        }.also { handler.post(it) }
    }

    private lateinit var progressDialog: AlertDialog
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView

    private fun showDownloadProgressDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.download_dialog, null)
        progressBar = dialogView.findViewById(R.id.progressBar)
        tvProgress = dialogView.findViewById(R.id.tvProgress)

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)

        progressDialog = dialogBuilder.create()
        progressDialog.show()
    }

    private fun updateProgressDialog(progress: Int) {
        progressBar.progress = progress
        tvProgress.text = "Downloading work... $progress%"
    }

    private fun dismissProgressDialog() {
        progressDialog.dismiss()
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        binding.backBtn.performClick()
    }
}