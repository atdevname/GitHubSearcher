package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.room.RepositoryCollectionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val downloadedListRepositoryEntity: Flow<List<RepositoryCollectionEntity>> =
        mainRepository.getAllDownloadedRepository()

    fun deleteItemDao(item: RepositoryCollectionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteItemDao(item)
        }
    }

    val groupEmptyListVisibility: LiveData<Boolean> =
        downloadedListRepositoryEntity.asLiveData(viewModelScope.coroutineContext)
            .map { it.isEmpty() }
    val recyclerVisibility: LiveData<Boolean> =
        downloadedListRepositoryEntity.asLiveData(viewModelScope.coroutineContext)
            .map { !it.isNullOrEmpty() }

}