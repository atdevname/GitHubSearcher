package com.atdev.githubproject.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.model.RepositoryJsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    /* Search Fragment */

    var searchField = MutableLiveData<String>()

    private var job: Job? = null
    var repositoryList: Flow<List<RepositoryJsonObject>>? = null

    private fun getSearchResult() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response =  mainRepository.getSearchResult(searchField.value.toString())
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    repositoryList = response.body()?.items
                } else {
                    Log.e("RETROFIT",response.message())
                }
            }
        }
    }


    fun fabClick(view: View) {
        if (searchField.value.isNullOrEmpty()) {
            getRequestField?.invoke()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                getSearchResult()
            }
        }
    }

    var getRequestField: (() -> Unit)? = null

    //val repositoryResult = MutableLiveData<List<RepositoryJsonObject>>()

}





