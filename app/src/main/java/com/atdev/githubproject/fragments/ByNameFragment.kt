package com.atdev.githubproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.adapters.FooterAdapter
import com.atdev.githubproject.adapters.SearchAdapter
import com.atdev.githubproject.databinding.FragmentByNameBinding
import com.atdev.githubproject.utils.ViewModelEvent
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.viewmodels.ByNameViewModel
import com.atdev.githubproject.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ByNameFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentByNameBinding

    private val byNameViewModel: ByNameViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_by_name,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = byNameViewModel
        binding.recycler.adapter = adapter?.withLoadStateFooter(FooterAdapter { adapter?.retry() })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        byNameViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        setupObservers()
        adapterStateListener()

        return binding.root
    }

    private fun setupObservers() {
        byNameViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.networkConnected.postValue(ViewModelEvent(it))
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let {
                byNameViewModel.searchByName(it)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                byNameViewModel.repositoryFlow?.collect {
                    adapter!!.submitData(it)
                }
            }
        })
    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        byNameViewModel.addItemInDao(item)
    }

    private fun adapterStateListener() {
        adapter?.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    binding.progressIndicator.visibility = View.INVISIBLE
                    binding.recycler.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.recycler.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    binding.progressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}