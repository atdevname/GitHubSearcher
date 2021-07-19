package com.atdev.githubproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.databinding.FragmentDownloadedBinding
import com.atdev.githubproject.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadedFragment : Fragment() {

    private lateinit var binding: FragmentDownloadedBinding
    private val viewModel:MainViewModel by viewModels()

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

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}