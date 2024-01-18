package com.example.stepupandroid.ui.login_sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityLoginBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel(this)

        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from").toString()
        }

        binding.loginBtn.setOnClickListener {
            var validated = true
            if(binding.email.text.isNullOrEmpty()){
                binding.email.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.email.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if(binding.password.text.isNullOrEmpty()){
                binding.password.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.error_color_border_drawable, null)
                binding.password.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                validated = false
            }
            if(!validated){
                return@setOnClickListener
            }
            val body = HashMap<String, String>()
            body["email"] = binding.email.text.toString()
            body["password"] = binding.password.text.toString()

            viewModel.login(body)
        }

        binding.email.addTextChangedListener {
            binding.email.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }
        binding.password.addTextChangedListener {
            binding.password.background =
                ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        viewModel.loginResultState.observe(this) {
            val intent = Intent(this, HomeActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            startActivity(intent)
            finishAffinity()
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }

    }

}