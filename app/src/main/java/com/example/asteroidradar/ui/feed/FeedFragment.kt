package com.example.asteroidradar.ui.feed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asteroidradar.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    private lateinit var viewModel: FeedViewModel
    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFeedBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = FeedAdapter(viewModel.pictureOfDay)
        binding.asteroidList.adapter = adapter
        adapter.addHeaderAndSubmitList(viewModel.asteroids.value)

        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            adapter.notifyItemChanged(0)
        }


        return binding.root
    }


}