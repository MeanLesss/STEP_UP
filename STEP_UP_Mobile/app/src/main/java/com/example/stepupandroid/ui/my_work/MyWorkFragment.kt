package com.example.stepupandroid.ui.my_work

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.adapter.MyWorkAdapter
import com.example.stepupandroid.databinding.FragmentMyWorkBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.MyWorkViewModel


class MyWorkFragment : Fragment(), MyWorkAdapter.OnWorkSelected {
    private lateinit var binding: FragmentMyWorkBinding
    private lateinit var viewModel: MyWorkViewModel

    override fun onWorkSelected(orderId: Int) {
        val intent = Intent(requireActivity(), MyWorkDetailActivity::class.java)
        intent.putExtra("orderId", orderId)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyWorkBinding.inflate(layoutInflater)

        if (Constants.UserRole == Constants.Freelancer || Constants.UserRole == Constants.Admin) {
            viewModel = MyWorkViewModel(requireActivity())

            initViewModel()

            viewModel.getMyWork()
        } else {
            binding.registerAsFreelancer.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun initViewModel() {
        viewModel.getMyWorkResultState.observe(requireActivity()) { result ->
            if (result.result.isNotEmpty()) {
                val adapter = MyWorkAdapter(requireActivity(), result.result, this)
                binding.myWorkRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.myWorkRecyclerView.adapter = adapter
            }
        }

        viewModel.errorResultState.observe(requireActivity()) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(childFragmentManager, "CustomDialog")
        }

    }

}