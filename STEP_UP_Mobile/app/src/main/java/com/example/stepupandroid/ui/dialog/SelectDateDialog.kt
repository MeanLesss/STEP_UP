package com.example.stepupandroid.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentSelectDateDialogBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.WelcomeActivity
import com.example.stepupandroid.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SelectDateDialog(private val onConfirm: (startDate: String, endDate: String) -> Unit) : DialogFragment() {
    private lateinit var binding: FragmentSelectDateDialogBinding

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectDateDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startDate.setOnClickListener { showDatePickerDialog(isStartDate = true) }
        binding.endDate.setOnClickListener { showDatePickerDialog(isStartDate = false) }
        binding.startDate.isFocusable = false
        binding.endDate.isFocusable = false
        binding.startDate.isLongClickable = false
        binding.endDate.isLongClickable = false

        binding.confirmBtn.setOnClickListener {
            var validated = true
            if (binding.startDate.text.isNullOrEmpty()) {
                binding.startDate.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.startDate.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.shake))
                validated = false
            }
            if (binding.endDate.text.isNullOrEmpty()) {
                binding.endDate.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.error_color_border_drawable,
                        null
                    )
                binding.endDate.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.shake))
                validated = false
            }
            if (!validated) {
                return@setOnClickListener
            }
            onConfirm(binding.startDate.text.toString(), binding.endDate.text.toString())
            dismiss() // Close the dialog
        }

        binding.startDate.addTextChangedListener {
            binding.startDate.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.endDate.addTextChangedListener {
            binding.endDate.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }
}