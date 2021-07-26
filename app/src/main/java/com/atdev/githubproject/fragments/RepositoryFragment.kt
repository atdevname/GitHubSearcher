package com.atdev.githubproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.activity.MainActivity
import com.atdev.githubproject.adapters.SearchRepositoryAdapter
import com.atdev.githubproject.databinding.FragmentRepositoryBinding
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryJsonObject
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentRepositoryBinding
    private val viewModel: RepositoryViewModel by activityViewModels()

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

        viewModel.notifyDataSetChanged = { notifyDataSetChanged() }

        binding.viewModel = viewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        (requireActivity() as MainActivity).hideOptionMenu()

        viewModel.repositoryList.observe(requireActivity(), {
            adapter?.dataSet = it
        })

        setVisibilityGroupListeners()

        return binding.root
    }

    private fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    override fun onItemDownloadClickListener(item: RepositoryJsonObject) {}

    override fun onItemAddClickListener(itemID: String) {
        viewModel.addItemInDao(itemID)
    }

    override fun onItemDeleteClickListener(itemID: String) {}

    private fun setVisibilityGroupListeners() {

        viewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            Log.i("TEST_REC", "$it")
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        viewModel.groupNotFoundVisibility.observe(viewLifecycleOwner, {
            Log.i("TEST_FOUND", "$it")
            if (it){
                binding.noFoundGroup.visibility = View.VISIBLE
                binding.emptyListGroup.visibility = View.INVISIBLE
            }
            else binding.noFoundGroup.visibility = View.INVISIBLE
        })

        viewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            Log.i("TEST_EMPTY", "$it")
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })

        viewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            Log.i("TEST_REC", "$it")
            if (it) binding.progressIndicator.visibility = View.VISIBLE
            else binding.progressIndicator.visibility = View.INVISIBLE
        })
    }
}