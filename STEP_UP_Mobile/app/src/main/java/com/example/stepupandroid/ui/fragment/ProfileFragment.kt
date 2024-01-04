package com.example.stepupandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stepupandroid.databinding.FragmentProfileBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.WelcomeActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.logoutBtn.setOnClickListener {
            SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.token)
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        return binding.root
    }

}