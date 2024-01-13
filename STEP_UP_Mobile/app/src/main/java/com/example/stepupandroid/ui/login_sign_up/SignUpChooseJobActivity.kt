package com.example.stepupandroid.ui.login_sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivitySignUpChooseJobBinding

class SignUpChooseJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpChooseJobBinding
    private var from = ""
    private var job = ""
    private val jobOptions by lazy {
        listOf(
            binding.student,
            binding.selfEmployment,
            binding.employee,
            binding.manager,
            binding.passionateWorker,
            binding.other
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpChooseJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        from = intent.getStringExtra("from").orEmpty()

        jobOptions.forEach { textView ->
            textView.setOnClickListener { updateSelection(it as TextView) }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("from", from)
            intent.putExtra("job", job)
            startActivity(intent)
        }


    }

    private fun updateSelection(selectedOption: TextView) {
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        val selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)

        jobOptions.forEach { it.background = defaultDrawable }
        selectedOption.background = selectedDrawable
        job = selectedOption.text.toString()
    }
}