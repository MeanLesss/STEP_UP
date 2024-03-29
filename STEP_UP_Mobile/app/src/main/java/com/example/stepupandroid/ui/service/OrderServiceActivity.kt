package com.example.stepupandroid.ui.service

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.DateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.databinding.ActivityOrderServiceBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.param.OrderServiceParam
import com.example.stepupandroid.model.param.OrderServiceSummaryParam
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.viewmodel.OrderServiceViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

class OrderServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderServiceBinding
    private lateinit var viewModel: OrderServiceViewModel

    private var serviceId by Delegates.notNull<Int>()

    private lateinit var adapter: AttachmentAdapter
    private val attachments = mutableListOf<Attachment>()

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val PICK_FILES_REQUEST_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = OrderServiceViewModel(this)

        initViewModel()

        serviceId = intent.getIntExtra("serviceId", 0)

        binding.startDate.setOnClickListener { showDatePickerDialog(isStartDate = true) }
        binding.endDate.setOnClickListener { showDatePickerDialog(isStartDate = false) }
        binding.startDate.isFocusable = false
        binding.endDate.isFocusable = false
        binding.startDate.isLongClickable = false
        binding.endDate.isLongClickable = false

        binding.description.setRawInputType(InputType.TYPE_CLASS_TEXT)

        adapter = AttachmentAdapter(attachments)
        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.attachmentRecyclerView.adapter = adapter

        binding.addMoreBtn.setOnClickListener {
            requestStoragePermission()
        }

        binding.cancelBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.summaryBtn.setOnClickListener {
            var validated = true
            if (binding.title.text.isNullOrEmpty()) {
                binding.title.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.description.text.isNullOrEmpty()) {
                binding.description.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.description.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.startDate.text.isNullOrEmpty()) {
                binding.startDate.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.startDate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.endDate.text.isNullOrEmpty()) {
                binding.endDate.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.endDate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (!validated) {
                return@setOnClickListener
            }
            val body = OrderServiceSummaryParam(
                service_id = serviceId.toString(),
                isAgreementAgreed = "1"
            )
            viewModel.getOrderSummary(body)
        }
        binding.title.addTextChangedListener {
            binding.title.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.description.addTextChangedListener {
            binding.description.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.startDate.addTextChangedListener {
            binding.startDate.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.endDate.addTextChangedListener {
            binding.endDate.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
    }

    private fun initViewModel(){
        viewModel.getOrderSummaryResultState.observe(this){
            val orderSummary = it.result
            val body = OrderServiceParam(
                service_id = serviceId.toString(),
                order_title = binding.title.text.toString(),
                order_description = binding.description.text.toString(),
                attachments = attachments,
                expected_start_date = binding.startDate.text.toString(),
                expected_end_date = binding.endDate.text.toString(),
                isAgreementAgreed = "1"
            )
            val intent = Intent(this, OrderServiceSummaryActivity::class.java)
            intent.putExtra("body", body)
            intent.putExtra("summary", orderSummary)
            startActivity(intent)
        }
        viewModel.errorResultState.observe(this){
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }
    }
    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                val formattedDate = dateFormat.format(selectedDate.time)

                if (isStartDate) {
                    startDate = selectedDate
                    binding.startDate.setText(formattedDate)
                } else {
                    endDate = selectedDate
                    binding.endDate.setText(formattedDate)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Setting min and max dates
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        if (isStartDate && endDate != null) {
            datePickerDialog.datePicker.maxDate = endDate!!.timeInMillis
        } else if (!isStartDate && startDate != null) {
            datePickerDialog.datePicker.minDate = startDate!!.timeInMillis
        }

        datePickerDialog.show()
    }

    private fun addAttachment(attachment: Attachment) {
        adapter.addAttachment(attachment)
    }

    private fun openFilePicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"  // Restrict to image files only
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"  // Restrict to image files only
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        }
        startActivityForResult(intent, PICK_FILES_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { returnIntent ->
                if (returnIntent.clipData != null) { // Multiple files selected
                    for (i in 0 until returnIntent.clipData!!.itemCount) {
                        val uri = returnIntent.clipData!!.getItemAt(i).uri
                        val fileName = getFileName(uri)
                        if (fileName != null) {
                            addAttachment(Attachment(fileName, uri))
                        }
                    }
                } else if (returnIntent.data != null) { // Single file selected
                    val uri = returnIntent.data!!
                    val fileName = getFileName(uri)
                    if (fileName != null) {
                        addAttachment(Attachment(fileName, uri))
                    }
                }
            }
        }
    }


    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) { // Check if the column index is valid
                        fileName = it.getString(columnIndex)
                    }
                }
            }
        } else if (uri.scheme == "file") {
            fileName = uri.path?.let { path ->
                File(path).name
            }
        }
        return fileName
    }

    private fun requestStoragePermission() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
        } else {
            openFilePicker()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openFilePicker()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}