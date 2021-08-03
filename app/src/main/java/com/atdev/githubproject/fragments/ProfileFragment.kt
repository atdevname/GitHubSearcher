package com.atdev.githubproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.adapters.SearchUsersAdapter
import com.atdev.githubproject.databinding.FragmentProfileBinding
import com.atdev.githubproject.viewmodels.ProfileViewModel
import com.atdev.githubproject.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        (requireActivity() as MainActivity).invalidateOptionsMenu()

        binding.viewModel = profileViewModel

        return binding.root
    }


}