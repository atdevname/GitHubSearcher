package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.model.RepositoryObjectDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

}
