package com.atdev.githubproject.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.profile.auth.data.LoginRepository
import com.atdev.githubproject.profile.auth.activity.LoginActivity
import com.atdev.githubproject.databinding.FragmentProfileBinding
import com.atdev.githubproject.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var loginRepository: LoginRepository

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

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

        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        profileViewModel.logoutStatus.observe(viewLifecycleOwner, {
            if (!loginRepository.isLoggedIn) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        })
    }

}