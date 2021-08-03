package com.atdev.githubproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.adapters.SearchUsersAdapter
import com.atdev.githubproject.databinding.FragmentByUserBinding
import com.atdev.githubproject.utils.ViewModelEvent
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.viewmodels.SharedViewModel
import com.atdev.githubproject.viewmodels.ByUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ByUserFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentByUserBinding

    private val byUserViewModel: ByUserViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchUsersAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_by_user,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = byUserViewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        byUserViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        setupObservers()
        setVisibilityGroupListeners()

        return binding.root
    }

    private fun setupObservers() {
        byUserViewModel.repositoryList.observe(viewLifecycleOwner, {
            adapter?.dataSet = it
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let { byUserViewModel.searchByName(it) }
        })

        byUserViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.networkConnected.postValue(ViewModelEvent(it))
        })

        byUserViewModel.foundByField.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.userByField.visibility = View.VISIBLE
                binding.userByField.text = "Found by: $it"
            } else {
                binding.userByField.visibility = View.INVISIBLE
            }
        })
    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        byUserViewModel.addItemInDao(item)
    }

    private fun setVisibilityGroupListeners() {

        byUserViewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        byUserViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            if (it) binding.progressIndicator.visibility = View.VISIBLE
            else binding.progressIndicator.visibility = View.INVISIBLE
        })
    }
}