package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentSearchServiceDialogBinding

class SearchServiceDialog : DialogFragment() {
    private lateinit var binding: FragmentSearchServiceDialogBinding
    private lateinit var serviceTypeSpinner: Spinner

    private var title = ""
    private var price = ""
    private var serviceType = ""

    private var listener: OnSearchListener? = null
    interface OnSearchListener {
        fun onSearch(title: String, price: String, serviceType: String)
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchServiceDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtn.setOnClickListener {
            title = binding.title.text.toString()
            price = binding.price.text.toString()
            listener?.onSearch(title, price, serviceType)
            dismiss() // Close the dialog
        }

        initServiceTypeSpinner()
    }

    private fun initServiceTypeSpinner() {
        serviceTypeSpinner = binding.serviceTypeSpinner
        val serviceTypes = arrayOf("Any", "Software Development", "Graphic Design")
        val serviceTypeAdapter = ArrayAdapter(
            requireActivity(), R.layout.item_spinner, serviceTypes)
        serviceTypeSpinner.adapter = serviceTypeAdapter

        serviceTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                serviceType = when (position) {
                    1 -> "Software Development"
                    2 -> "Graphic Design"
                    else -> "" // "All" is selected
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}