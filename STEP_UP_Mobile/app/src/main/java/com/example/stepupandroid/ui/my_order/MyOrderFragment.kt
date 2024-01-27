package com.example.stepupandroid.ui.my_order

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.adapter.MyOrderAdapter
import com.example.stepupandroid.databinding.FragmentMyOrderBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.MyOrderViewModel

class MyOrderFragment : Fragment(), MyOrderAdapter.OnOrderSelected {
    private lateinit var binding: FragmentMyOrderBinding
    private lateinit var viewModel: MyOrderViewModel

    override fun onOrderSelected(orderId: Int) {
        val intent = Intent(requireActivity(), MyOrderDetailActivity::class.java)
        intent.putExtra("orderId", orderId)
        startActivity(intent)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyOrderBinding.inflate(layoutInflater)


        viewModel = MyOrderViewModel(requireActivity())

        initViewModel()

        viewModel.getMyWork()


        return binding.root
    }

    private fun initViewModel() {
        viewModel.getMyOrderResultState.observe(requireActivity()) { result ->
            if (result.result.isNotEmpty()) {
                val adapter = MyOrderAdapter(requireActivity(), result.result, this)
                binding.myOrderRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.myOrderRecyclerView.adapter = adapter
            }
        }

        viewModel.errorResultState.observe(requireActivity()) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(childFragmentManager, "CustomDialog")
        }

    }
}