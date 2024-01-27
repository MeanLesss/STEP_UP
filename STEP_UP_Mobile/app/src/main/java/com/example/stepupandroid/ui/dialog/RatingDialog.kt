package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.databinding.FragmentRatingDialogBinding

class RatingDialog(private val onFinish: (rating: Float) -> Unit)  : DialogFragment() {
    private lateinit var binding: FragmentRatingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.finishBtn.setOnClickListener {
            onFinish(binding.ratingBar.rating)
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