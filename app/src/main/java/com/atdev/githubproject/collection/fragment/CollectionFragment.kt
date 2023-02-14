package com.atdev.githubproject.collection.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.R
import com.atdev.githubproject.collection.adapter.CollectionListAdapter
import com.atdev.githubproject.collection.viewmodel.CollectionViewModel
import com.atdev.githubproject.databinding.FragmentCollectionBinding
import com.atdev.githubproject.search.listeners.AdapterDeleteItemClickListener
import com.atdev.githubproject.search.model.RepositoryCollectionEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CollectionFragment : Fragment(), AdapterDeleteItemClickListener {

    private lateinit var binding: FragmentCollectionBinding

    private val viewModel: CollectionViewModel by viewModels()

    private val adapter by lazy { CollectionListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initVM()
    }

    private fun initViews() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.setEmptyView(binding.emptyGroup)
    }

    private fun initVM() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.savedRepository.collect {
                adapter.dataSet = it
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clearAll -> {
                viewModel.onDeleteAllClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemDeleteClickListener(item: RepositoryCollectionEntity) {
        viewModel.onDeleteItemClicked(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.collection_menu, menu)
    }
}