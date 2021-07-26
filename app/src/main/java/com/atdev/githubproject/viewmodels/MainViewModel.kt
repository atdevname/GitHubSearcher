package com.atdev.githubproject.viewmodels

import androidx.lifecycle.ViewModel
import com.atdev.githubproject.helpers.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

}





