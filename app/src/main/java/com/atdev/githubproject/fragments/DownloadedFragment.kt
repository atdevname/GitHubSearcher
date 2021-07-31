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
        (requireActivity() as MainActivity).hideOptionMenu()

        binding.viewModel = downloadedViewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.Main) {
            downloadedViewModel.downloadedListRepositoryDownloadedEntity.collect {
                adapter?.dataSet = it
            }
        }

        setVisibilityGroupListeners()
        return binding.root
    }


    override fun onItemDeleteClickListener(itemID: String) {
        downloadedViewModel.deleteItemDao(itemID)
        adapter?.notifyDataSetChanged()
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

}