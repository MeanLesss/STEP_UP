package com.example.stepupandroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepupandroid.adapter.MyWorkAdapter
import com.example.stepupandroid.databinding.FragmentMyWorkBinding
import com.example.stepupandroid.model.MyWork
import java.util.Random


class MyWorkFragment : Fragment() {
    private lateinit var binding: FragmentMyWorkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyWorkBinding.inflate(layoutInflater)

        binding.myWorkRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Sample data for testing
        val itemList = mutableListOf<MyWork>()
        for (i in 1..50) {
            val item = MyWork(
                "Title $i",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.  ........ $i",
                "Name $i",
                "Start Date $i",
                "End Date $i",
                getRandomStatus()
            )
            itemList.add(item)
        }

        // Set the adapter
        val adapter = MyWorkAdapter(requireActivity(), itemList)
        binding.myWorkRecyclerView.adapter = adapter


        return binding.root
    }

    private val statusOptions = listOf("completed", "canceled", "in progress")
    fun getRandomStatus(): String {
        val random = Random()
        val randomIndex = random.nextInt(statusOptions.size)
        return statusOptions[randomIndex]
    }

}