package com.dicoding.myeventapp.ui.upcoming

import UpcomingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myeventapp.data.response.ListEventsItem
import com.dicoding.myeventapp.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        upcomingViewModel.getEvents()
    }

    private fun observeViewModel() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            val adapter = UpcomingAdapter(events).apply {
                setOnItemClickCallback(object : UpcomingAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListEventsItem) {
                        navigateToDetail(data.id)
                    }
                })
            }
            binding.rvUpcomingEvents.adapter = adapter
        }



        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDetail(eventId: Int?) {

        val action = UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(eventId ?: -1)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}