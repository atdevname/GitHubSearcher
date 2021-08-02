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
import com.atdev.githubproject.activity.MainActivity
import com.atdev.githubproject.adapters.FooterAdapter
import com.atdev.githubproject.adapters.SearchByUserAdapter
import com.atdev.githubproject.databinding.FragmentRepositoryBinding
import com.atdev.githubproject.helpers.ViewModelEvent
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import com.atdev.githubproject.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoryFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentRepositoryBinding

    private val repositoryViewModel: RepositoryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchByUserAdapter(this) } }

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

        binding.viewModel = repositoryViewModel

        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.adapter = adapter?.withLoadStateFooter(FooterAdapter { adapter?.retry() })

        repositoryViewModel.notifyDataSetChanged = {
            adapter?.notifyDataSetChanged()
        }

        (requireActivity() as MainActivity).hideOptionMenu()

        setupObservers()
        adapterStateListener()
        setVisibilityGroupListeners()

        return binding.root
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

    private fun setupObservers() {

        repositoryViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.setNetworkConnected(ViewModelEvent(it))
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let {
                repositoryViewModel.searchByName(it)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                repositoryViewModel.repositoryFlow?.collect {
                    adapter!!.submitData(it)
                }
            }
        })

    }

    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        repositoryViewModel.addItemInDao(item)
    }


    private fun setVisibilityGroupListeners() {
//        repositoryViewModel.groupNotFoundVisibility.observe(viewLifecycleOwner, {
//            if (it) {
//                binding.noFoundGroup.visibility = View.VISIBLE
//                binding.emptyListGroup.visibility = View.INVISIBLE
//            } else binding.noFoundGroup.visibility = View.INVISIBLE
//        })
//
//        repositoryViewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
//            if (it) binding.emptyListGroup.visibility = View.VISIBLE
//            else binding.emptyListGroup.visibility = View.INVISIBLE
//        })
    }
}