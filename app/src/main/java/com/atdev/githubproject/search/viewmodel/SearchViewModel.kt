package com.atdev.githubproject.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.search.model.RepositoryObjectDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _repositoryList = Channel<List<RepositoryObjectDto>>()
    val repositoryList = _repositoryList.receiveAsFlow()

    private val _progressBarEnabled = MutableLiveData(false)
    val progressBarEnabled = _progressBarEnabled as LiveData<Boolean>

    private val _error = MutableLiveData<Throwable>()
    val error = _error as LiveData<Throwable>

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _progressBarEnabled.postValue(false)
        _error.postValue(exception)
    }

    fun onSearchClicked(value: String) = getSearchResult(value)

    private fun getSearchResult(value: String) {
        _progressBarEnabled.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            mainRepository.getReposByName(value)
                .mapNotNull { response ->
                    response.body()?.items
                }
                .onCompletion {
                    _progressBarEnabled.postValue(false)
                }
                .collect { list ->
                    _repositoryList.send(list)
                }
        }
    }

    fun onSaveRepositoryClicked(item: RepositoryObjectDto) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.saveRep(item.transformItemInDao())
        }
    }
}
