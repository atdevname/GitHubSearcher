package com.atdev.githubproject.fragments

import android.os.Bundle
import android.util.Log
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
import com.atdev.githubproject.adapters.SearchRepositoryAdapter
import com.atdev.githubproject.adapters.SearchUsersAdapter
import com.atdev.githubproject.databinding.FragmentUsersBinding
import com.atdev.githubproject.helpers.ViewModelEvent
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.viewmodels.SharedViewModel
import com.atdev.githubproject.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentUsersBinding

    private val usersViewModel: UsersViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //private val adapter by lazy { activity?.let { SearchUsersAdapter(this) } }
    private val adapter by lazy { activity?.let { SearchRepositoryAdapter(this) } }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_users,
            container,
            false
        )

        binding.viewModel = usersViewModel
        binding.recycler.adapter = adapter?.withLoadStateFooter(FooterAdapter { adapter?.retry() })
        // binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        usersViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        (requireActivity() as MainActivity).hideOptionMenu()

        setupObservers()
        adapterStateListener()

        return binding.root
    }

    private fun setupObservers() {

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let {
                usersViewModel.searchByUser(it)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                usersViewModel.repositoryFlow?.collect {
                    adapter!!.submitData(it)
                }
            }
        })

        usersViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.setNetworkConnected(ViewModelEvent(it))
        })

        usersViewModel.foundByField.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.userByField.visibility = View.VISIBLE
                binding.userByField.text = "Found by: $it"
            } else {
                binding.userByField.visibility = View.INVISIBLE
            }
        })
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


    override fun onItemAddClickListener(item: RepositoryObjectDto) {
        usersViewModel.addItemInDao(item)
    }
}