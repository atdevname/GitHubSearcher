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
import com.atdev.githubproject.listeners.AdapterDeleteItemClickListener
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.room.RepositoryDownloadedEntity
import com.atdev.githubproject.viewmodels.DownloadedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DownloadedFragment : Fragment(), AdapterDeleteItemClickListener {

    private lateinit var binding: FragmentDownloadedBinding

    private val downloadedViewModel: DownloadedViewModel by viewModels()

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
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = downloadedViewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.Main) {
            downloadedViewModel.downloadedListRepositoryEntity.collect {
                adapter?.dataSet = it
            }
        }
        setVisibilityGroupListeners()
        return binding.root
    }


    private fun setVisibilityGroupListeners() {

        downloadedViewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        downloadedViewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })
    }

    override fun onItemDeleteClickListener(item: RepositoryDownloadedEntity) {
        downloadedViewModel.deleteItemDao(item)
        adapter?.notifyDataSetChanged()
    }
}