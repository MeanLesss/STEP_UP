package com.example.stepupandroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stepupandroid.databinding.FragmentMyServiceBinding

class MyServiceFragment : Fragment() {
    private lateinit var binding: FragmentMyServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyServiceBinding.inflate(layoutInflater)

        return binding.root
    }

}