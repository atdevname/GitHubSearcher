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
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.adapters.DownloadedListAdapter
import com.atdev.githubproject.databinding.FragmentCollectionBinding
import com.atdev.githubproject.listeners.AdapterDeleteItemClickListener
import com.atdev.githubproject.model.RepositoryCollectionEntity
import com.atdev.githubproject.viewmodels.CollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionFragment : Fragment(), AdapterDeleteItemClickListener {

    private lateinit var binding: FragmentCollectionBinding

    private val collectionViewModel: CollectionViewModel by viewModels()

    private val adapter by lazy { activity?.let { DownloadedListAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collection,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = collectionViewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.Main) {
            collectionViewModel.downloadedListRepositoryEntity.collect {
                adapter?.dataSet = it
            }
        }

        setVisibilityGroupListeners()

        return binding.root
    }


    private fun setVisibilityGroupListeners() {

        collectionViewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        collectionViewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })
    }

    override fun onItemDeleteClickListener(item: RepositoryCollectionEntity) {
        collectionViewModel.deleteItemDao(item)
        adapter?.notifyDataSetChanged()
    }
}