package com.dicoding.myeventapp.ui.finish

import FinishAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myeventapp.data.response.ListEventsItem
import com.dicoding.myeventapp.databinding.FragmentFinishBinding

class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!

    private val finishViewModel: FinishViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(context)

        finishViewModel.getEvents()
    }

    private fun observeViewModel() {
        finishViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            val adapter = FinishAdapter(events, true).apply {
                setOnItemClickCallback(object : FinishAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListEventsItem) {
                        navigateToDetail(data.id)
                    }
                })
            }
            binding.rvFinishedEvents.adapter = adapter
        }

        finishViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDetail(eventId: Int?) {
        val action = FinishFragmentDirections.actionFinishFragmentToDetailFragment(eventId ?: -1)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
