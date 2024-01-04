package com.example.stepupandroid.helper

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentCustomDialogBinding

class CustomDialog(private val title: String, private val description : String, private val type : String) : DialogFragment() {

    private lateinit var binding: FragmentCustomDialogBinding
    var onDismissListener: (() -> Unit)? = null
    var button = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if(title.isNotEmpty()) {
            binding.dialogTitle.text = title
        }else{
            binding.dialogTitle.visibility = View.GONE
        }

        if(description.isNotEmpty()) {
            binding.dialogDescription.text = description
        }else{
            binding.dialogDescription.visibility = View.GONE
        }

        when (type) {
            Constants.WARNING -> {
                binding.dialogIcon.setImageResource(R.drawable.icon_warning)
            }
            Constants.ERROR -> {
                binding.dialogIcon.setImageResource(R.drawable.icon_error)
            }
            Constants.SUCCESS -> {
                binding.dialogIcon.setImageResource(R.drawable.icon_success1)
            }
            else -> {
                binding.dialogIcon.visibility = View.GONE
            }
        }

        binding.btnOkay.setOnClickListener {
            dialog?.dismiss()
        }

        if(button.isNotEmpty()){
            if(button == "invisible"){
                binding.btnOkay.visibility = View.GONE
            }else {
                binding.btnOkay.text = button
            }
        }

        return binding.root
    }

    fun dismissDialog(){
        dialog?.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

}