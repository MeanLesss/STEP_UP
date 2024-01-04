package com.example.stepupandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.stepupandroid.databinding.ActivityWelcomeBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.SharedPreferenceUtil

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

        binding.continueAsGuest.setOnClickListener {
            SharedPreferenceUtil().addToSp(
                ApiKey.SharedPreferenceKey.isGuest,
                "true"
            )
            //todo
            SharedPreferenceUtil().addToSp(
                ApiKey.SharedPreferenceKey.token,
                "250|cLZDm3W2nR1RSCqfsoGcHwpp2uJBN88MRfE8gS0gb2314d66"
            )
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.signInWithGoogle.setOnClickListener {
            //todo
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
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