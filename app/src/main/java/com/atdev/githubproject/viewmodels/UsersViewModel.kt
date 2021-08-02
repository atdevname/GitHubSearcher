package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.model.RepositoryObjectDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var repositoryFlow: Flow<PagingData<RepositoryObjectDto>>? = null

    fun searchByUser(value: String) {
        repositoryFlow = mainRepository.getPagingDataByUser(value).cachedIn(viewModelScope)
    }

    fun addItemInDao(item: RepositoryObjectDto) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addItemInDao(mainRepository.transformItemInDao(item))
        }
    }

    var networkConnected = MutableLiveData<Boolean>()
    var foundByField = MutableLiveData("")

    var notifyDataSetChanged: (() -> Unit)? = null

}
