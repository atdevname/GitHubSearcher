package com.atdev.githubproject.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atdev.githubproject.MainActivity
import com.atdev.githubproject.R
import com.atdev.githubproject.databinding.FragmentProfileBinding
import com.atdev.githubproject.profile.auth.activity.LoginActivity
import com.atdev.githubproject.profile.auth.data.LoginRepository
import com.atdev.githubproject.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var loginRepository: LoginRepository

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

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

        setupObserver()

        binding.darkModeSwitch.isChecked = viewModel.getValue()

        binding.viewModel = viewModel

        Log.i("TEST_VM_FRAGEMENT",viewModel.getValue().toString())

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.setQuery(binding.darkModeSwitch.isChecked)
        Log.i("TEST_PAUSE",binding.darkModeSwitch.isChecked.toString())
    }

    private fun setupObserver() {
        viewModel.logoutStatus.observe(viewLifecycleOwner, {
            if (!loginRepository.isLoggedIn) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        })
    }

}