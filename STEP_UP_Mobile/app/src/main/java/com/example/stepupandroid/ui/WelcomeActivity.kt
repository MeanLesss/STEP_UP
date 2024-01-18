package com.example.stepupandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.stepupandroid.databinding.ActivityWelcomeBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.login_sign_up.LoginActivity
import com.example.stepupandroid.ui.login_sign_up.SignUpAsGuestActivity
import com.example.stepupandroid.ui.login_sign_up.SignUpChooseJobActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.visibility = View.GONE

        if (SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.isGuest) == "true") {
            binding.continueAsGuest.visibility = View.GONE
            binding.loginToUseFeature.visibility = View.VISIBLE
        }

        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from").toString()
            binding.backBtn.visibility = View.VISIBLE
        }

        if (!SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.token)
                .isNullOrEmpty()
        ) {
            if (SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.isGuest) == "true") {
                if (from.isEmpty()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

        }

        binding.signInWithEmail.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            startActivity(intent)
        }

        binding.singUpWithEmail.setOnClickListener {
            val intent = Intent(this, SignUpChooseJobActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            startActivity(intent)
        }

        binding.continueAsGuest.setOnClickListener {
            val intent = Intent(this, SignUpAsGuestActivity::class.java)
            startActivity(intent)
        }

        binding.signInWithGoogle.setOnClickListener {
            //todo
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("from", "welcome")
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (from.isNotEmpty()) {
            binding.backBtn.performClick()
        } else {
            finish()
        }
    }
}