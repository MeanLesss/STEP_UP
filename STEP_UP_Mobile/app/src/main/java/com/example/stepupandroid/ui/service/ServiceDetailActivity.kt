package com.example.stepupandroid.ui.service

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.ViewPagerAdapter
import com.example.stepupandroid.databinding.ActivityServiceDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.viewmodel.ServiceDetailViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.stfalcon.imageviewer.StfalconImageViewer

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var viewModel: ServiceDetailViewModel

    private var imageUrls: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ServiceDetailViewModel(this)
        initViewModel()

        Fresco.initialize(this)

        val serviceId = intent.getIntExtra("serviceId", 0)

        viewModel.getServiceDetail(serviceId)

        binding.backBtn.setOnClickListener {
            finish()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.serviceDetailResultState.observe(this) { result ->
            if (result.result.attachments.isNotEmpty()) {
                imageUrls = result.result.attachments.values.toMutableList()
                val adapter = ViewPagerAdapter(imageUrls) { imageUrl ->
                    showFullscreenImage(imageUrl)
                }
                binding.imageViewPager.adapter = adapter
                binding.indicator.setViewPager(binding.imageViewPager)
            }

            binding.title.text = result.result.title
            binding.rating.rating = result.result.service_rate.toFloat()
            binding.serviceType.text = result.result.service_type
            binding.description.text = result.result.description
            binding.price.text = "$" + result.result.price
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

    }

    private fun showFullscreenImage(selectedPosition: Int) {
        val overlayView =
            LayoutInflater.from(this).inflate(R.layout.fullscreen_overlay_layout, null)
        val closeButton = overlayView.findViewById<ImageButton>(R.id.closeButton)

        val viewer = StfalconImageViewer.Builder(this, imageUrls) { view, image ->
            Glide.with(this)
                .load(image)
                .error(R.drawable.step_up_logo)
                .into(view)
        }
            .withStartPosition(selectedPosition)
            .withBackgroundColor(Color.parseColor("#AA000000"))
            .withHiddenStatusBar(false)
            .withOverlayView(overlayView)
            .withTransitionFrom(binding.placeholderImage)
            .show()

        closeButton.setOnClickListener {
            viewer.dismiss()
        }
    }

}