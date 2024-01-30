package com.example.stepupandroid.ui.my_order

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.ResourceAdapter
import com.example.stepupandroid.databinding.ActivityMyOrderDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.DownloadAttachment
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.CancelDialog
import com.example.stepupandroid.ui.dialog.ConfirmDialog
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.dialog.RatingDialog
import com.example.stepupandroid.viewmodel.OrderDetailViewModel
import java.io.File

class MyOrderDetailActivity : AppCompatActivity(),
    CancelDialog.OnCancelListener, ResourceAdapter.OnDownloadClick {
    private lateinit var binding: ActivityMyOrderDetailBinding
    private lateinit var viewModel: OrderDetailViewModel

    private var isShow = true
    private var isUpdate = false

    private var orderId = 0
    private var serviceId = 0
    private var status = ""
    private lateinit var completedAttachment: DownloadAttachment
    private lateinit var clientAttachment: DownloadAttachment
    private var downloading = ""

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

        binding.cancelBtn.setOnClickListener {
            if (status == Constants.Pending) {
                val dialog =
                    ConfirmDialog("Are you sure you want to cancel?") {
                        val body = HashMap<String, String>()
                        body["order_id"] = orderId.toString()
                        body["service_id"] = serviceId.toString()
                        viewModel.cancelOrderPending(body)
                    }
                dialog.show(supportFragmentManager, "ConfirmDialog")
            } else if (status == Constants.InProgress) {
                val dialog =
                    CancelDialog("If you cancel, you will only receive a refund of 50% of your payment.")
                dialog.setOnCancelListener(this)
                dialog.show(supportFragmentManager, "CancelDialog")
            }
        }

        binding.confirmButton.setOnClickListener {
            val dialog = RatingDialog { rating ->
                val body = HashMap<String, String>()
                body["order_id"] = orderId.toString()
                body["service_id"] = serviceId.toString()
                body["rate"] = rating.toString()
                viewModel.confirmOrder(body)
            }
            dialog.show(supportFragmentManager, "RatingDialog")
        }

        binding.downloadWork.setOnClickListener {
            downloading = "completedAttachment"
            checkPermissionAndDownload()
        }
    }

    override fun onDownloadClick(attachment: DownloadAttachment) {
        clientAttachment = attachment
        downloading = "clientAttachment"
        checkPermissionAndDownload()
    }

    override fun onCancel(description: String) {
        val body = HashMap<String, String>()
        body["order_id"] = orderId.toString()
        body["service_id"] = serviceId.toString()
        body["cancel_desc"] = description
        viewModel.cancelOrderInProgress(body)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.orderDetailResultState.observe(this) { result ->
            serviceId = result.result.service_id
            status = result.result.stringStatus

            if (result.result.order_attachments.isNotEmpty()) {
                val resourceList: MutableList<DownloadAttachment> =
                    result.result.order_attachments.map { (fileName, fileUrl) ->
                        DownloadAttachment(fileName, fileUrl)
                    }.toMutableList()
                val adapter = ResourceAdapter(resourceList)
                adapter.setOnDownloadClick(this)
                binding.resourceRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.resourceRecyclerView.adapter = adapter
            } else {
                binding.resource.visibility = View.GONE
            }

            if (result.result.completed_attachments.isNotEmpty()) {
                completedAttachment = DownloadAttachment(
                    result.result.completed_attachments.keys.firstOrNull().toString(),
                    result.result.completed_attachments.values.firstOrNull().toString()
                )

                binding.fileName.text = completedAttachment.fileName

                binding.downloadLayout.visibility = View.VISIBLE
            } else {
                binding.downloadLayout.visibility = View.GONE
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
            binding.confirmBtnLayout.visibility = View.GONE
            if (result.result.stringStatus == Constants.Pending || result.result.stringStatus == Constants.InProgress) {
                binding.cancelBtnLayout.visibility = View.VISIBLE
            } else if (result.result.stringStatus == Constants.InReview) {
                binding.confirmBtnLayout.visibility = View.VISIBLE
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
        val downloadFile = if (downloading == "completedAttachment") {
            completedAttachment
        }else{
            clientAttachment
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            downloadFile.fileUrl
        )
        if (file.exists()) {
            file.delete()
        }

        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(downloadFile.fileName)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

        val request = DownloadManager.Request(Uri.parse(downloadFile.fileUrl))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadFile.fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDescription("Downloading work...")
        request.setTitle("Download")
        request.setTitle(downloadFile.fileName)
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
        tvProgress.text = "Downloading attachment... $progress%"
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