package com.example.stepupandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.adapter.MyServiceAdapter
import com.example.stepupandroid.databinding.FragmentMyServiceBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.ui.my_service.CreateServiceActivity
import com.example.stepupandroid.viewmodel.MyServiceViewModel

class MyServiceFragment : Fragment() {
    private lateinit var binding: FragmentMyServiceBinding
    private lateinit var viewModel: MyServiceViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyServiceBinding.inflate(layoutInflater)
        viewModel = MyServiceViewModel(requireActivity())

        initViewModel()

        viewModel.getMyService()

        binding.addBtn.setOnClickListener{
            requireActivity().startActivity(Intent(requireActivity(), CreateServiceActivity::class.java))
        }


        return binding.root
    }

    private fun initViewModel() {
        viewModel.getMyServiceResultState.observe(requireActivity()) { result ->
            if (result.result.isNotEmpty()) {
                val adapter =  MyServiceAdapter(requireActivity(), result.result)
                binding.myServiceRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.myServiceRecyclerView.adapter = adapter
            }
        }

        viewModel.errorResultState.observe(requireActivity()) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(childFragmentManager, "CustomDialog")
        }

    }

}