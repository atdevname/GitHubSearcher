package com.atdev.githubproject.collection.viewmodel

import androidx.lifecycle.*
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.search.model.RepositoryCollectionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val savedRepository: Flow<List<RepositoryCollectionEntity>> =
        mainRepository.getAllSavedRepository().flowOn(Dispatchers.IO)

    fun onDeleteItemClicked(item: RepositoryCollectionEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteSavedRep(item)
        }

    fun onDeleteAllClicked() =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteAllDownloaded()
        }
}