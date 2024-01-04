package com.example.stepupandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.stepupandroid.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.visibility = View.GONE

        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from").toString()
            binding.backBtn.visibility = View.VISIBLE
            binding.continueAsGuest.visibility = View.GONE
        }

        binding.signInWithEmail.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            if(from.isNotEmpty()){
                intent.putExtra("from", from)
            }
            startActivity(intent)
        }

        binding.signInWithGoogle.setOnClickListener {
            //todo
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        binding.backBtn.performClick()
    }
}