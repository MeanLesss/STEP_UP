package com.example.stepupandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentMyOrderBinding
import com.example.stepupandroid.databinding.FragmentProfileBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        if(SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.token).isNullOrEmpty()){
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra("from", "Profile")
            requireActivity().startActivity(intent)
        }

        binding.logoutBtn.setOnClickListener {
            SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.token)
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        return binding.root
    }

}