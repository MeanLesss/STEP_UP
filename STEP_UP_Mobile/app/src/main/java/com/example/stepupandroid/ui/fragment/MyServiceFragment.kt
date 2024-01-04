package com.example.stepupandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentMyServiceBinding
import com.example.stepupandroid.databinding.FragmentProfileBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.LoginActivity

class MyServiceFragment : Fragment() {
    private lateinit var binding: FragmentMyServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyServiceBinding.inflate(layoutInflater)

        if(SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.token).isNullOrEmpty()){
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra("from", "MyService")
            requireActivity().startActivity(intent)
        }

        return binding.root
    }

}