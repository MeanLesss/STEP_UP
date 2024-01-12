package com.example.stepupandroid.ui.my_service

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.adapter.AttachmentAdapter
import com.example.stepupandroid.databinding.ActivityCreateServiceBinding
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.param.CreateServiceParam
import java.io.File

class CreateServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateServiceBinding

    private lateinit var adapter: AttachmentAdapter
    private val attachments = mutableListOf<Attachment>()
    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val PICK_FILES_REQUEST_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AttachmentAdapter(attachments)
        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.attachmentRecyclerView.adapter = adapter

        binding.addMoreBtn.setOnClickListener {
            requestStoragePermission()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.summaryBtn.setOnClickListener {
            val body = CreateServiceParam(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                status = "0",
                attachments = attachments,
                price = binding.price.text.toString(),
                service_type = "Software Development",
                start_date = binding.startDate.text.toString(),
                end_date = binding.endDate.text.toString()
            )
            val intent = Intent(this, CreateServiceSummaryActivity::class.java)
            intent.putExtra("body", body)
            startActivity(intent)
        }
    }

    private fun addAttachment(attachment: Attachment) {
        adapter.addAttachment(attachment)
    }

    private fun openFilePicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), STORAGE_PERMISSION_CODE)
        } else {
            openFilePicker()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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