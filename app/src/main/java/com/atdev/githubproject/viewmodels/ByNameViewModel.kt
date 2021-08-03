package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.model.RepositoryObjectDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ByNameViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var networkConnected = MutableLiveData<Boolean>()

    var repositoryFlow: Flow<PagingData<RepositoryObjectDto>>? = null

    fun searchByName(value: String) {
        repositoryFlow = mainRepository.getPagingDataByName(value).cachedIn(viewModelScope)
    }

    fun addItemInDao(item: RepositoryObjectDto) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addItemInDao(item.transformItemInDao())
        }
    }

    var notifyDataSetChanged: (() -> Unit)? = null
}
