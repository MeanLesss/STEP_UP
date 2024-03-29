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
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.my_order.MyOrderFragment
import com.example.stepupandroid.ui.my_service.MyServiceFragment
import com.example.stepupandroid.ui.my_work.MyWorkFragment
import com.example.stepupandroid.ui.profile.ProfileFragment
import com.example.stepupandroid.ui.service.ServiceFragment
import com.example.stepupandroid.viewmodel.ProfileViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var viewModel: ProfileViewModel

    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        from = intent.getStringExtra("from").orEmpty()
        selectNavItem()

        if (Constants.UserRole == 0) {
            viewModel = ProfileViewModel(this)
            initViewModel()
            viewModel.getUser()
        } else {

            fragmentManager = supportFragmentManager

            binding.navigation.setOnItemSelectedListener { item ->
                handleNavigation(item.itemId)
                true
            }
            selectNavItem()
        }
    }

    private fun selectNavItem(){
        if (from.isNotEmpty()) {
            // Set default selected item based on 'from'
            val selectedItemId = when (from) {
                Constants.MyWork -> R.id.navigation_my_work
                Constants.MyService -> R.id.navigation_my_service
                Constants.MyOrder -> R.id.navigation_my_order
                Constants.Profile -> R.id.navigation_profile
                else -> R.id.navigation_service
            }
            binding.navigation.selectedItemId = selectedItemId
        } else {
            binding.navigation.selectedItemId = R.id.navigation_service
        }
    }

    private fun initViewModel() {
        viewModel.getUserResultState.observe(this) { results ->
            Constants.UserRole = results.user_info.role
            recreate()
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Error)
            customDialog.onDismissListener = {
                SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.token)
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            customDialog.show(supportFragmentManager, "CustomDialog")
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
        startActivityForResult(intent, 1)
    }

    private fun getFragmentName(fragment: Fragment): String {
        return when (fragment) {
            is MyWorkFragment -> Constants.MyWork
            is MyServiceFragment -> Constants.MyService
            is MyOrderFragment -> Constants.MyOrder
            is ProfileFragment -> Constants.Profile
            else -> ""
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
//        transaction.addToBackStack(null) // Optional, allows going back to previous fragment
        transaction.commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data?.getStringExtra("from") == "welcome") {
                binding.navigation.setOnItemSelectedListener(null)
                binding.navigation.selectedItemId = R.id.navigation_service
                binding.navigation.setOnItemSelectedListener { item ->
                    handleNavigation(item.itemId)
                    true
                }
            }
        }
    }
}