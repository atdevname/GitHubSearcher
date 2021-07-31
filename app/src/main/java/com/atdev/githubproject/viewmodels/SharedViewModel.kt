package com.atdev.githubproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atdev.githubproject.helpers.ViewModelEvent

class SharedViewModel () : ViewModel() {

    val searchValue = MutableLiveData<ViewModelEvent<String>>()

    var networkConnected = MutableLiveData<ViewModelEvent<Boolean>>()
    fun setNetworkConnected(value: ViewModelEvent<Boolean>) {
        networkConnected.value = value
    }


}