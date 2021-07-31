package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.room.RepositoryDownloadedEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val downloadedListRepositoryDownloadedEntity: Flow<List<RepositoryDownloadedEntity>> = mainRepository.getAllDownloadedRepository()

    fun deleteItemDao(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadedListRepositoryDownloadedEntity.map { it.find { item -> item.id == itemId } }
                .filterNotNull().collect {
                    mainRepository.deleteItemDao(it)
                }
        }
    }

    val groupEmptyListVisibility: LiveData<Boolean> =
        downloadedListRepositoryDownloadedEntity.asLiveData(viewModelScope.coroutineContext).map { it.isEmpty() }
    val recyclerVisibility: LiveData<Boolean> =
        downloadedListRepositoryDownloadedEntity.asLiveData(viewModelScope.coroutineContext).map { !it.isNullOrEmpty() }

}