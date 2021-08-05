package com.atdev.githubproject.search.fragment

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
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.search.adapter.FooterAdapter
import com.atdev.githubproject.search.adapter.SearchAdapter
import com.atdev.githubproject.databinding.FragmentSearchBinding
import com.atdev.githubproject.search.listeners.AdapterItemClickListener
import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.atdev.githubproject.components.utils.ViewModelEvent
import com.atdev.githubproject.search.viewmodel.SearchViewModel
import com.atdev.githubproject.components.shareviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = searchViewModel
        binding.recycler.adapter = adapter?.withLoadStateFooter(FooterAdapter { adapter?.retry() })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        searchViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        setupObservers()
        adapterStateListener()

        return binding.root
    }

    private fun setupObservers() {
        searchViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.networkConnected.postValue(ViewModelEvent(it))
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let {
                searchViewModel.searchByName(it)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                searchViewModel.repositoryFlow?.collect {
                    adapter!!.submitData(it)
                }
            }
        })
    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        searchViewModel.addItemInDao(item)
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