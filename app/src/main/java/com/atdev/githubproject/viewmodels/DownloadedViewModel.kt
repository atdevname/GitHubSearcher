package com.atdev.githubproject.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.model.RepositoryJsonObject
import com.atdev.githubproject.room.RepositoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val downloadedList: Flow<List<RepositoryEntity>> = mainRepository.getAllRepository()

    fun deleteItemDao(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadedList.map { it.find { item -> item.id == itemId } }
                .filterNotNull().collect {
                    mainRepository.deleteItemDao(it)
                }
        }
    }

    val groupEmptyListVisibility: LiveData<Boolean> =
        downloadedList.asLiveData(viewModelScope.coroutineContext).map { it.isEmpty() }
    val recyclerVisibility: LiveData<Boolean> =
        downloadedList.asLiveData(viewModelScope.coroutineContext).map { !it.isNullOrEmpty() }

}