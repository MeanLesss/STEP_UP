package com.example.stepupandroid.ui.my_service

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.example.stepupandroid.R
import com.example.stepupandroid.adapter.ViewPagerAdapter
import com.example.stepupandroid.databinding.ActivityMyServiceDetailBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.param.UpdateServiceStatusParam
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.ConfirmDialog
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.ui.dialog.SelectDateDialog
import com.example.stepupandroid.viewmodel.MyServiceDetailViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.stfalcon.imageviewer.StfalconImageViewer

class MyServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyServiceDetailBinding
    private lateinit var viewModel: MyServiceDetailViewModel

    private var imageUrls: MutableList<String> = mutableListOf()

    private var isUpdate = false

    private var serviceId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MyServiceDetailViewModel(this)
        initViewModel()

        Fresco.initialize(this)

        val serviceId = intent.getIntExtra("serviceId", 0)

        viewModel.getServiceDetail(serviceId)

        binding.backBtn.setOnClickListener {
            if (isUpdate) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("from", Constants.MyService)
                startActivity(intent)
                finishAffinity()
            } else {
                finish()
            }
        }

        binding.deactivateBtn.setOnClickListener {
            val dialog =
                ConfirmDialog("Are you sure you want to deactivate?") {
                    val body = UpdateServiceStatusParam(
                        serviceId.toString(),
                        false,
                        "",
                        ""
                    )
                    viewModel.updateServiceStatus(body)
                }
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }

        binding.activateBtn.setOnClickListener {
            val dialog =
                SelectDateDialog { startDate, endDate ->
                    val body = UpdateServiceStatusParam(
                        serviceId.toString(),
                        true,
                        Util.convertDate("MMM dd, yyyy", "yyyy-MM-dd", startDate),
                        Util.convertDate("MMM dd, yyyy", "yyyy-MM-dd", endDate)
                    )
                    viewModel.updateServiceStatus(body)
                }
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.serviceDetailResultState.observe(this) { result ->
            serviceId = result.result.id

            if (result.result.attachments.isNotEmpty()) {
                imageUrls = result.result.attachments.values.toMutableList()
                val adapter = ViewPagerAdapter(imageUrls) { imageUrl ->
                    showFullscreenImage(imageUrl)
                }
                binding.imageViewPager.adapter = adapter
                binding.indicator.setViewPager(binding.imageViewPager)
            } else {
                imageUrls.add("")
                val adapter = ViewPagerAdapter(imageUrls) { imageUrl ->
                    showFullscreenImage(imageUrl)
                }
                binding.imageViewPager.adapter = adapter
            }

            binding.title.text = result.result.title
            binding.rating.rating = result.result.service_rate.toFloat()
            binding.serviceType.text = result.result.service_type
            binding.description.text = result.result.description
            binding.price.text = "$" + Util.formatStringToDecimal(result.result.price)

            if (result.result.discount == 0f) {
                binding.discountLayout.visibility = View.GONE
            } else {
                binding.discountLayout.visibility = View.VISIBLE
                binding.discount.text = String.format("%.2f", result.result.discount) + "%"
            }

            binding.activateBtn.visibility = View.GONE
            binding.deactivateBtn.visibility = View.GONE
            if (result.result.stringStatus == Constants.Inactive) {
                binding.activateBtn.visibility = View.VISIBLE
            } else if(result.result.stringStatus == Constants.Active){
                binding.deactivateBtn.visibility = View.VISIBLE
            }
        }

        viewModel.updateServiceStatusResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                viewModel.getServiceDetail(serviceId)
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
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

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        binding.backBtn.performClick()
    }
}