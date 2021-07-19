package com.atdev.githubproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.adapters.SearchResultListAdapter
import com.atdev.githubproject.databinding.FragmentSearchBinding
import com.atdev.githubproject.room.RepositoryEntity
import com.atdev.githubproject.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel:MainViewModel by viewModels()

    private val adapter by lazy { activity?.let { SearchResultListAdapter() } }

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

        binding.viewModel = viewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.getRequestField = { getRequestField() }


        (requireActivity() as MainActivity).hideOptionMenu()

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.repositoryList?.collect {
                adapter?.dataSet = it
            }
        }

        return binding.root
    }

    private fun getRequestField() {
        binding.search.requestFocus()
        val imm = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.showSoftInput(binding.search, InputMethodManager.SHOW_IMPLICIT)
    }

}