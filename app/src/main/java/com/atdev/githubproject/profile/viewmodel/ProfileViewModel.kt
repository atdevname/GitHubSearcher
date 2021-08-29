package com.atdev.githubproject.profile.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.atdev.githubproject.profile.auth.data.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val state : SavedStateHandle,
    private val loginRepository: LoginRepository,
    ) : ViewModel() {

    private val savedStateHandle = state

    private val testvar : MutableLiveData<Boolean> = savedStateHandle.getLiveData("TEST")

    fun getValue(): Boolean {
        return testvar.value ?: false
    }

    fun setQuery(value: Boolean) {
        savedStateHandle.set("TEST", value)
        Log.i("TEST_VM_SET_QUERY",value.toString())
    }

    var username = MutableLiveData<String>(loginRepository.user?.displayName)
    var secondName = MutableLiveData<String>()

    var logoutStatus = MutableLiveData<Boolean>(loginRepository.isLoggedIn)

    fun logout(view:View){
        loginRepository.logout()
        logoutStatus.postValue(true)
    }

}
