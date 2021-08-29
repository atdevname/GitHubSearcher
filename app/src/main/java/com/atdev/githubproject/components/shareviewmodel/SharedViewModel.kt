package com.atdev.githubproject.components.shareviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atdev.githubproject.components.utils.ViewModelEvent

class SharedViewModel () : ViewModel() {
    val searchValue = MutableLiveData<ViewModelEvent<String>>()
    var networkConnected = MutableLiveData<ViewModelEvent<Boolean>>()
}