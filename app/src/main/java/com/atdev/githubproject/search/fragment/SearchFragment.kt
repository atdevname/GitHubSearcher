package com.atdev.githubproject.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.components.shareviewmodel.SharedViewModel
import com.atdev.githubproject.databinding.FragmentSearchBinding
import com.atdev.githubproject.search.adapter.SearchAdapter
import com.atdev.githubproject.search.api.NoConnectivityException
import com.atdev.githubproject.search.listeners.AdapterItemClickListener
import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.atdev.githubproject.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { SearchAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeVMs()
    }

    private fun initViews() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.setEmptyView(binding.emptyGroup)
    }

    private fun observeVMs() {

        searchViewModel.apply {
            lifecycleScope.launch(Dispatchers.Main) {
                repositoryList.collect {
                    adapter.dataSet = it
                }
            }

            progressBarEnabled.observe(viewLifecycleOwner) {
                binding.progressIndicator.isVisible = it
            }

            error.observe(viewLifecycleOwner) {

                val errorText = if (it is NoConnectivityException) {
                    getString(R.string.no_internet_toast)
                } else {
                    getString(R.string.common_error_toast)
                }

                Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
            }
        }

        sharedViewModel.searchValue.observe(viewLifecycleOwner) { event ->
            event.getValueOnceOrNull()?.let {
                searchViewModel.onSearchClicked(it)
            }
        }
    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        searchViewModel.onSaveRepositoryClicked(item)
    }
}