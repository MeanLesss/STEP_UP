package com.example.stepupandroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityHomeBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.SharedPreferenceUtil
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

        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from").toString()
        }

        binding.navigation.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId)
            true
        }

        // Set default selected item
        if (from.isNotEmpty()) {
            when (from) {
                "MyWork" -> binding.navigation.selectedItemId = R.id.navigation_my_work
                "MyService" -> binding.navigation.selectedItemId = R.id.navigation_my_service
                "MyOrder" -> binding.navigation.selectedItemId = R.id.navigation_my_order
                "Profile" -> binding.navigation.selectedItemId = R.id.navigation_profile
            }
        } else {
            binding.navigation.selectedItemId = R.id.navigation_service
        }
    }

    private fun handleNavigation(itemId: Int) {
        val navigationMap = mapOf(
            R.id.navigation_my_work to Pair(MyWorkFragment(), R.string.my_work),
            R.id.navigation_my_service to Pair(MyServiceFragment(), R.string.my_service),
            R.id.navigation_service to Pair(ServiceFragment(), R.string.service),
            R.id.navigation_my_order to Pair(MyOrderFragment(), R.string.my_order),
            R.id.navigation_profile to Pair(ProfileFragment(), R.string.profile)
        )

        navigationMap[itemId]?.let { (fragment, titleResId) ->
            if (fragment !is ServiceFragment) {
                if (SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.token)
                        .isNullOrEmpty() || SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.isGuest) == "true"
                ) {
                    redirectToWelcome(getFragmentName(fragment))
                } else {
                    replaceFragment(fragment)
                    binding.fragmentTitle.text = getString(titleResId)
                }
            } else {
                replaceFragment(fragment)
                binding.fragmentTitle.text = getString(titleResId)
            }
        }
    }

    private fun redirectToWelcome(fromFragment: String) {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("from", fromFragment)
        startActivity(intent)
    }

    private fun getFragmentName(fragment: Fragment): String {
        return when (fragment) {
            is MyWorkFragment -> "MyWork"
            is MyServiceFragment -> "MyService"
            is MyOrderFragment -> "MyOrder"
            is ProfileFragment -> "Profile"
            else -> ""
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
//        transaction.addToBackStack(null) // Optional, allows going back to previous fragment
        transaction.commit()
    }
}