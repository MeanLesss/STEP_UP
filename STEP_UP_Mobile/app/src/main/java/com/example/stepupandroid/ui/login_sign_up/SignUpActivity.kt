package com.example.stepupandroid.ui.login_sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivitySignUpBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.ui.SuccessActivity
import com.example.stepupandroid.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    private var from = ""
    private var job = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = SignUpViewModel(this)

        from = intent.getStringExtra("from").orEmpty()
        job = intent.getStringExtra("job").orEmpty()



        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            var validated = true
            if (binding.username.text.isNullOrEmpty()) {
                binding.username.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.username.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.email.text.isNullOrEmpty()) {
                binding.email.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.email.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.password.text.isNullOrEmpty()) {
                binding.password.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.password.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.confirmPassword.text.isNullOrEmpty()) {
                binding.confirmPassword.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.confirmPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if (binding.phoneNumber.text.isNullOrEmpty()) {
                binding.phoneNumber.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.phoneNumber.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }

            if(!validated){
                return@setOnClickListener
            }

            val body = SignUpParam(
                guest = false,
                freelancer = false,
                name = binding.username.text.toString(),
                email = binding.email.text.toString(),
                password = binding.password.text.toString(),
                confirm_password = binding.confirmPassword.text.toString(),
                phone_number = binding.phoneNumber.text.toString(),
                id_number = binding.idNumber.text.toString(),
                job_type = job
            )

            viewModel.signUp(body)
        }

        binding.username.addTextChangedListener {
            binding.username.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.email.addTextChangedListener {
            binding.email.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.password.addTextChangedListener {
            binding.password.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.confirmPassword.addTextChangedListener {
            binding.confirmPassword.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.phoneNumber.addTextChangedListener {
            binding.phoneNumber.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }


        viewModel.signUpResultState.observe(this) {
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("title", resources.getString(R.string.account_has_been_created))
            startActivity(intent)
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }
    }
}