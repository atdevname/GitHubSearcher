package com.atdev.githubproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atdev.githubproject.helpers.ViewModelEvent

class SharedViewModel () : ViewModel() {

    val searchValue = MutableLiveData<ViewModelEvent<String>>()

    private var _networkConnected = MutableLiveData<Boolean>()
    val networkConnected: LiveData<Boolean>
        get() = _networkConnected

    fun setNetworkConnected(value: Boolean) {
        _networkConnected.value = value
    }


}