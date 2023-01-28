package com.example.asteroidradar.ui.feed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.asteroidradar.R
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
        val viewModelFactory = FeedViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[FeedViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = FeedAdapter(
            viewModel.pictureOfDay,
            FeedAdapter.clickListener {
                viewModel.displayAsteroidDetails(it)
                viewModel.displayAsteroidDetailsComplete()
            }
        )

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController()
                    .navigate(FeedFragmentDirections.actionFeedFragmentToDetailFragment(it))
            }
        }

        binding.asteroidList.adapter = adapter
        adapter.addHeaderAndSubmitList(viewModel.asteroids.value)

        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            adapter.notifyItemChanged(0)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        val menuHost = (requireActivity() as MenuHost)
        menuHost.addMenuProvider(
            object : MenuProvider {

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.filter_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.filter_all -> {
                            viewModel.filterAsteroidsByAll()
                        }
                        R.id.filter_week -> {
                            viewModel.filterAsteroidsByWeek()
                        }
                        R.id.filter_today -> {
                            viewModel.filterAsteroidsByToday()
                        }
                    }
                    return true
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

}