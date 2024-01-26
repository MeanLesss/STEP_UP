package com.example.stepupandroid.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityRegisterAsFreelancerBinding
import com.example.stepupandroid.databinding.ActivitySignUpChooseJobBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.response.GetUserResponse
import com.example.stepupandroid.ui.SuccessActivity
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.ProfileViewModel
import com.example.stepupandroid.viewmodel.SignUpViewModel

class RegisterAsFreelancerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterAsFreelancerBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var signUpViewModel: SignUpViewModel

    private var from = ""

    private lateinit var user: GetUserResponse
    private var idType = ""
    private val idTypes by lazy {
        listOf(
            binding.idCard,
            binding.passport
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsFreelancerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ProfileViewModel(this)
        signUpViewModel = SignUpViewModel(this)
        initViewModel()

        viewModel.getUser()

        from = intent.getStringExtra("from").orEmpty()

        binding.signUpBtn.isEnabled = false

        idTypes.forEach { textView ->
            textView.setOnClickListener {
                updateSelection(it as TextView)
                binding.signUpBtn.isEnabled = true
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            val body = SignUpParam(
                guest = false,
                freelancer = true,
                name = user.user_info.name,
                email = user.user_info.email,
                password = "",
                confirm_password = "",
                phone_number = user.user_detail.phone,
                job_type = user.user_detail.job_type
            )
            signUpViewModel.signUp(body)
        }
    }

    private fun initViewModel() {
        viewModel.getUserResultState.observe(this) { results ->
            user = results
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }

        signUpViewModel.signUpResultState.observe(this) {
            val intent = Intent(this, SuccessActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            intent.putExtra("title", resources.getString(R.string.registered_as_freelancer))
            startActivity(intent)
        }

        signUpViewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }
    }

    private fun updateSelection(selectedOption: TextView) {
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        val selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)

        idTypes.forEach { it.background = defaultDrawable }
        selectedOption.background = selectedDrawable
        idType = selectedOption.text.toString()
    }
}
