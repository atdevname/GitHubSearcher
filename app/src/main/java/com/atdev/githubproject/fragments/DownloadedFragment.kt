package com.atdev.githubproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.activity.MainActivity
import com.atdev.githubproject.adapters.DownloadedListAdapter
import com.atdev.githubproject.databinding.FragmentDownloadedBinding
import com.atdev.githubproject.helpers.setVisibility
import com.atdev.githubproject.listeners.AdapterItemClickListener
import com.atdev.githubproject.model.RepositoryJsonObject
import com.atdev.githubproject.viewmodels.DownloadedViewModel
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DownloadedFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentDownloadedBinding

    private val viewModel: DownloadedViewModel by viewModels()
    private val repositoryViewModel: RepositoryViewModel by viewModels()

    private val adapter by lazy { activity?.let { DownloadedListAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_downloaded,
            container,
            false
        )
        (requireActivity() as MainActivity).hideOptionMenu()

        binding.viewModel = viewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.downloadedList.collect {
                adapter?.dataSet = it
            }
        }

        return binding.root

    }

    override fun onItemDownloadClickListener(item: RepositoryJsonObject) {
    }

    override fun onItemAddClickListener(itemID: String) {
        TODO("Not yet implemented")
    }

    override fun onItemDeleteClickListener(itemID: String) {
        viewModel.deleteItemDao(itemID)
        adapter?.notifyDataSetChanged()

        repositoryViewModel.resetStatusAdded(itemID)
    }

}