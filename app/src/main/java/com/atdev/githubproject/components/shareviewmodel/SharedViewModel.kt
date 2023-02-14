package com.atdev.githubproject.components.shareviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atdev.githubproject.components.utils.ViewModelEvent

class SharedViewModel : ViewModel() {

    private val _searchValue = MutableLiveData<ViewModelEvent<String>>()
    val searchValue = _searchValue as LiveData<ViewModelEvent<String>>

    fun onSetSearchQuery(value: String) = _searchValue.postValue(ViewModelEvent(value))
}