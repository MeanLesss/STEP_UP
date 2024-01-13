package com.example.stepupandroid.ui.my_service

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
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.databinding.ActivityCreateServiceBinding
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.param.CreateServiceParam
import java.io.File
import java.util.Calendar

class CreateServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateServiceBinding

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
        binding = ActivityCreateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startDate.setOnClickListener { showDatePickerDialog(isStartDate = true) }
        binding.endDate.setOnClickListener { showDatePickerDialog(isStartDate = false) }
        binding.startDate.isFocusable = false
        binding.endDate.isFocusable = false
        binding.startDate.isLongClickable = false
        binding.endDate.isLongClickable = false

        adapter = AttachmentAdapter(attachments)
        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.attachmentRecyclerView.adapter = adapter

        val spinnerAdapter = ArrayAdapter(
            this, R.layout.item_spinner, resources.getStringArray(R.array.service_type_spinner)
        )
        binding.serviceType.adapter = spinnerAdapter

        binding.addMoreBtn.setOnClickListener {
            requestStoragePermission()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.summaryBtn.setOnClickListener {
            var validated = true
            if (binding.title.text.isNullOrEmpty()) {
                binding.title.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.description.text.isNullOrEmpty()) {
                binding.description.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.description.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
//            if (attachments.isEmpty()) {
//                binding.attachmentLayout.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
//                binding.attachmentLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
//                validated = false
//            }
            if (binding.price.text.isNullOrEmpty()) {
                binding.price.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.price.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.serviceType.selectedItem.toString().isEmpty() || binding.serviceType.selectedItem.toString() == "Select A Service Type") {
                binding.serviceType.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.serviceType.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.startDate.text.isNullOrEmpty()) {
                binding.startDate.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.startDate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.endDate.text.isNullOrEmpty()) {
                binding.endDate.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.endDate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if(!validated){
                return@setOnClickListener
            }
                val body = CreateServiceParam(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                attachments = attachments,
                price = binding.price.text.toString(),
                service_type = binding.serviceType.selectedItem.toString(),
                start_date = binding.startDate.text.toString(),
                end_date = binding.endDate.text.toString()
            )
            val intent = Intent(this, CreateServiceSummaryActivity::class.java)
            intent.putExtra("body", body)
            startActivity(intent)
        }

        binding.title.addTextChangedListener {
            binding.title.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.description.addTextChangedListener {
            binding.description.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.price.addTextChangedListener {
            binding.price.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.serviceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.serviceType.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
            }

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

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            if (isStartDate) {
                startDate = selectedDate
                binding.startDate.setText(DateFormat.getDateInstance().format(selectedDate.time))
            } else {
                endDate = selectedDate
                binding.endDate.setText(DateFormat.getDateInstance().format(selectedDate.time))
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        // Setting min date
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
                type = "*image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*image/*"
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