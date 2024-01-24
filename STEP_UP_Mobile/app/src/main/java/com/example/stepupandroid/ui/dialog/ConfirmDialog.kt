package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.databinding.FragmentConfirmDialogBinding

class ConfirmDialog(private val description: String, private val onConfirm: () -> Unit) : DialogFragment() {
    private lateinit var binding: FragmentConfirmDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(description.isNotEmpty()){
            binding.description.text = description
        }else{
            binding.description.visibility = View.GONE
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.confirmBtn.setOnClickListener {
            onConfirm()
            dismiss() // Close the dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

}