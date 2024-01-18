package com.example.stepupandroid.ui.service

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stepupandroid.adapter.ServiceAdapter
import com.example.stepupandroid.databinding.FragmentServiceBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.viewmodel.ServiceViewModel

class ServiceFragment : Fragment(), ServiceAdapter.OnServiceSelected {
    private lateinit var binding: FragmentServiceBinding
    private lateinit var viewModel: ServiceViewModel

    private lateinit var adapter: ServiceAdapter
    private val range = 10
    private var page = 1
    private var isLoading = false // Flag to track loading state
    private var isLastPage = false // Flag to track if it's the last page
    private var isShown = false // Flag to track custom dialog

    override fun onServiceSelected(serviceId: Int) {
        val intent = Intent(requireActivity(), ServiceDetailActivity::class.java)
        intent.putExtra("serviceId", serviceId)
        startActivity(intent)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentServiceBinding.inflate(layoutInflater)
        viewModel = ServiceViewModel(requireActivity())

        initViewModel()

        // Initialize the RecyclerView and adapter
        initRecyclerView()

        // Load the initial data
        loadServiceData()

        // Listen for scroll events on the RecyclerView
        binding.serviceRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Check if it's not already loading and not the last page
                if (!isLoading && !isLastPage) {
                    // Check if the user has scrolled to the last item
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        // Load more data
                        loadServiceData()
                    }
                } else if (!recyclerView.canScrollVertically(1)) {
                    if (!isShown) {
                        isShown = true
//                        val customDialog = CustomDialog("", "No More Item", Constants.WARNING)
//                        customDialog.onDismissListener = {
//                            isShown = false
//                        }
//                        customDialog.show(childFragmentManager, "CustomDialog")
                    }
                }
            }
        })
        return binding.root
    }

    private fun initRecyclerView() {
        adapter =
            ServiceAdapter(requireActivity(), mutableListOf(), this) // Initialize with an empty list
        binding.serviceRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.serviceRecyclerView.adapter = adapter
    }

    private fun initViewModel() {

        viewModel.getServiceResultState.observe(requireActivity()) { result ->
            isLoading = false // Reset loading flag
            if (result.result.data.isNotEmpty()) {
                // Append the new data to the adapter
                adapter.addData(result.result.data)

                //Increment for next page
                page++
            } else {
                isLastPage = true // Mark it as the last page if no more data is available
            }
        }

        viewModel.errorResultState.observe(requireActivity()) {
            isLoading = false // Reset loading flag
            val customDialog = CustomDialog("", it, Constants.Warning)
            customDialog.show(childFragmentManager, "CustomDialog")
        }

    }

    private fun loadServiceData() {
        isLoading = true // Set loading flag to true
        val body = GetServiceParam("", range, page)
        viewModel.getService(body)
    }
}