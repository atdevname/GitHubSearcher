package com.atdev.githubproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atdev.githubproject.utils.ViewModelEvent

class SharedViewModel () : ViewModel() {

    val searchValue = MutableLiveData<ViewModelEvent<String>>()

    var networkConnected = MutableLiveData<ViewModelEvent<Boolean>>()



}