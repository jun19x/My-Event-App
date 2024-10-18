package com.dicoding.myeventapp.ui.home

import EventAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myeventapp.data.response.ListEventsItem
import com.dicoding.myeventapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(context)

        homeViewModel.getEvents()
    }

    private fun observeViewModel() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            val adapter = EventAdapter(events).apply {
                setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListEventsItem) {
                        navigateToDetail(data.id)
                    }
                })
            }
            binding.rvUpcomingEvents.adapter = adapter
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            val adapter = EventAdapter(events, true).apply {
                setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListEventsItem) {
                        navigateToDetail(data.id)
                    }
                })
            }
            binding.rvFinishedEvents.adapter = adapter
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDetail(eventId: Int?) {

        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(eventId ?: -1)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
