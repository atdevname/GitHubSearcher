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
import com.atdev.githubproject.adapters.SearchUsersAdapter
import com.atdev.githubproject.databinding.FragmentUsersBinding
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.viewmodels.SharedViewModel
import com.atdev.githubproject.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentUsersBinding

    private val usersViewModel: UsersViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy { activity?.let { SearchUsersAdapter(this) } }

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
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        usersViewModel.notifyDataSetChanged = { adapter?.notifyDataSetChanged() }

        (requireActivity() as MainActivity).hideOptionMenu()

        setupObservers()
        setVisibilityGroupListeners()

        return binding.root
    }

    private fun setupObservers() {
        usersViewModel.repositoryList.observe(viewLifecycleOwner, {
            adapter?.dataSet = it
        })

        sharedViewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let { usersViewModel.searchByName(it) }
        })

        usersViewModel.networkConnected.observe(viewLifecycleOwner, {
            sharedViewModel.setNetworkConnected(it)
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

    override fun onItemAddClickListener(itemID: String) {
        usersViewModel.addItemInDao(itemID)
    }

    private fun setVisibilityGroupListeners() {

        usersViewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        usersViewModel.groupNotFoundVisibility.observe(viewLifecycleOwner, {
            if (it) {
                binding.noFoundGroup.visibility = View.VISIBLE
                binding.emptyListGroup.visibility = View.INVISIBLE
            } else binding.noFoundGroup.visibility = View.INVISIBLE
        })

        usersViewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })

        usersViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            if (it) binding.progressIndicator.visibility = View.VISIBLE
            else binding.progressIndicator.visibility = View.INVISIBLE
        })
    }
}