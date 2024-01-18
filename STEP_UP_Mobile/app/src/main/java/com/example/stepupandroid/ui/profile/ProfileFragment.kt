package com.example.stepupandroid.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stepupandroid.databinding.FragmentProfileBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.WelcomeActivity
import com.example.stepupandroid.ui.dialog.LogoutDialog
import com.example.stepupandroid.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        viewModel = ProfileViewModel(requireActivity())

        viewModel.getUser()

        binding.logoutBtn.setOnClickListener {
            LogoutDialog().show(childFragmentManager, "LogoutDialog")
        }

        viewModel.getUserResultState.observe(requireActivity()) {results ->
            binding.username.text = results.user_info.name
            binding.email.text = results.user_info.email
            binding.phoneNumber.text = "0" + results.user_detail.phone
            binding.job.text = results.user_detail.job_type
            binding.idNumber.text = results.user_detail.id_card_no
        }

        viewModel.errorResultState.observe(requireActivity()) {
            CustomDialog("", it, Constants.Error).show(childFragmentManager, "CustomDialog")
        }

        return binding.root
    }

}