package com.atdev.githubproject.profile.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.atdev.githubproject.profile.auth.data.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    ) : ViewModel() {

    var switchDarkMode =  MutableLiveData<Boolean>(false)

    var username = MutableLiveData<String>(loginRepository.user?.displayName)
    var secondName = MutableLiveData<String>()

    var logoutStatus = MutableLiveData<Boolean>(loginRepository.isLoggedIn)

    fun logout(view:View){
        loginRepository.logout()
        logoutStatus.postValue(true)
    }

}
