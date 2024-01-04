package com.example.stepupandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityHomeBinding
import com.example.stepupandroid.ui.fragment.MyOrderFragment
import com.example.stepupandroid.ui.fragment.MyServiceFragment
import com.example.stepupandroid.ui.fragment.MyWorkFragment
import com.example.stepupandroid.ui.fragment.ProfileFragment
import com.example.stepupandroid.ui.fragment.ServiceFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager

    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = supportFragmentManager

        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from").toString()
        }

        binding.navigation.setOnNavigationItemSelectedListener { item ->
            // Logic for selecting items
            when (item.itemId) {
                R.id.navigation_my_work -> {
                    replaceFragment(MyWorkFragment())
                    binding.fragmentTitle.text = getString(R.string.my_work)
                }
                R.id.navigation_my_service -> {
                    replaceFragment(MyServiceFragment())
                    binding.fragmentTitle.text = getString(R.string.my_service)
                }
                R.id.navigation_service -> {
                    replaceFragment(ServiceFragment())
                    binding.fragmentTitle.text = getString(R.string.service)
                }
                R.id.navigation_my_order -> {
                    replaceFragment(MyOrderFragment())
                    binding.fragmentTitle.text = getString(R.string.my_order)
                }
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment())
                    binding.fragmentTitle.text = getString(R.string.profile)
                }
            }
            true
        }

        // Set default selected item
        if(from.isNotEmpty()){
            when(from){
                "MyWork" -> binding.navigation.selectedItemId = R.id.navigation_my_work
                "MyService" -> binding.navigation.selectedItemId = R.id.navigation_my_service
                "MyOrder" -> binding.navigation.selectedItemId = R.id.navigation_my_order
                "Profile" -> binding.navigation.selectedItemId = R.id.navigation_profile
            }
        }else {
            binding.navigation.selectedItemId = R.id.navigation_service
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null) // Optional, allows going back to previous fragment
        transaction.commit()
    }
}