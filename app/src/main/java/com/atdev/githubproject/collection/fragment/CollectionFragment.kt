package com.atdev.githubproject.collection.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.collection.adapter.CollectionListAdapter
import com.atdev.githubproject.collection.viewmodel.CollectionViewModel
import com.atdev.githubproject.databinding.FragmentCollectionBinding
import com.atdev.githubproject.search.listeners.AdapterDeleteItemClickListener
import com.atdev.githubproject.search.model.RepositoryCollectionEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionFragment : Fragment(), AdapterDeleteItemClickListener {

    private lateinit var binding: FragmentCollectionBinding

    private val viewModel: CollectionViewModel by viewModels()

    private val adapter by lazy { activity?.let { CollectionListAdapter(this) } }

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

        binding.viewModel = viewModel
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.downloadedListRepositoryEntity.collect {
                adapter?.dataSet = it
            }
        }

        setHasOptionsMenu(true)

        setVisibilityGroupListeners()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.collection_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clearAll -> {
                viewModel.deleteAllDao()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setVisibilityGroupListeners() {
        viewModel.recyclerVisibility.observe(viewLifecycleOwner, {
            if (it) binding.recycler.visibility = View.VISIBLE
            else binding.recycler.visibility = View.INVISIBLE
        })

        viewModel.groupEmptyListVisibility.observe(viewLifecycleOwner, {
            if (it) binding.emptyListGroup.visibility = View.VISIBLE
            else binding.emptyListGroup.visibility = View.INVISIBLE
        })
    }

    override fun onItemDeleteClickListener(item: RepositoryCollectionEntity) {
        viewModel.deleteItemDao(item)
    }


}