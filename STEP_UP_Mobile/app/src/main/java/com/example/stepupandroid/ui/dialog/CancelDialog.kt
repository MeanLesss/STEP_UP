package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentCancelDialogBinding

class CancelDialog(private val warningText: String) : DialogFragment() {
    private lateinit var binding: FragmentCancelDialogBinding

    private var listener: OnCancelListener? = null
    interface OnCancelListener {
        fun onCancel(description: String)
    }

    fun setOnCancelListener(listener: OnCancelListener) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCancelDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(warningText.isNotEmpty()){
            binding.warningText.text = warningText
        }else{
            binding.warningText.visibility = View.GONE
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.confirmBtn.setOnClickListener {
            if(binding.description.text.isEmpty()){
                binding.description.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.shake))
            }else{
                listener?.onCancel(binding.description.text.toString())
                dismiss() // Close the dialog
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

}