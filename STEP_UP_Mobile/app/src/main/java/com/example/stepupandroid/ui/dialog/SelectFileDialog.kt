package com.example.stepupandroid.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentSelectFileDialogBinding
import com.example.stepupandroid.model.Attachment

class SelectFileDialog : DialogFragment() {
    private lateinit var binding: FragmentSelectFileDialogBinding

    private var selectedFileUri: Uri? = null

    private var listener: OnFileSelectedListener? = null
    interface OnFileSelectedListener {
        fun onFileSelected(attachment: Attachment)
    }

    fun setOnFileSelectedListener(listener: OnFileSelectedListener) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectFileDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectFile.setOnClickListener {
            openFileSelector()
        }

        binding.btnUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun openFileSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip", "application/rar"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_SELECT_FILE)
    }

    private fun uploadFile() {
        selectedFileUri?.let {uri ->
            val fileName = getFileName(uri)
            val attachment = Attachment(fileName, uri)
            listener?.onFileSelected(attachment)
            dismiss() // Close the dialog
        } ?: run {
            // Show error message or prompt to select a file
            binding.btnSelectFile.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.shake))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            selectedFileUri?.let { uri ->
                binding.txtFileName.text = getFileName(uri)
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var fileName = ""
        context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }

    companion object {
        private const val REQUEST_SELECT_FILE = 1
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
        }
    }
}