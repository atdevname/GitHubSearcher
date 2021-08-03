package com.atdev.githubproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.activity.MainActivity
import com.atdev.githubproject.adapters.SearchRepositoryAdapter
import com.atdev.githubproject.databinding.FragmentRepositoryBinding
import com.atdev.githubproject.helpers.ViewModelEvent
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import com.atdev.githubproject.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentRepositoryBinding

    private val repositoryViewModel: RepositoryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchRepositoryAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_repository,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = repositoryViewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        repositoryViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        setupObservers()
        setVisibilityGroupListeners()

        return binding.root
    }

    private fun setupObservers() {
        repositoryViewModel.repositoryList.observe(viewLifecycleOwner, {
            adapter?.dataSet = it
        })

        repositoryViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.networkConnected.postValue(ViewModelEvent(it))
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let { repositoryViewModel.searchByName(it) }
        })

    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        repositoryViewModel.addItemInDao(item)
    }

    private fun setVisibilityGroupListeners() {

        repositoryViewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        repositoryViewModel.groupNotFoundVisibility.observe(viewLifecycleOwner, {
            if (it) {
                binding.noFoundGroup.visibility = View.VISIBLE
                binding.emptyListGroup.visibility = View.INVISIBLE
            } else binding.noFoundGroup.visibility = View.INVISIBLE
        })

        repositoryViewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })

        repositoryViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            if (it) binding.progressIndicator.visibility = View.VISIBLE
            else binding.progressIndicator.visibility = View.INVISIBLE
        })
    }
}