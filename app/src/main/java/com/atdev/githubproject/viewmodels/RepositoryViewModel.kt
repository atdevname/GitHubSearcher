package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.model.RepositoryJsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var repositoryList = MutableLiveData<List<RepositoryJsonObject>>(ArrayList())
    private var job: Job? = null
    private var responseEmpty = MutableLiveData<Boolean>(false)

    private fun getSearchResult(value: String) {
        _progressBarVisibility.postValue(true)
        job = viewModelScope.launch {
            val response = mainRepository.getSearchRepository(value)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    response.body()?.items?.let {
                        repositoryList.postValue(it)
                        if (it.isNotEmpty()) responseEmpty.postValue(false)
                        else responseEmpty.postValue(true)
                        _progressBarVisibility.postValue(false)
                    }
                } else {
                    job?.cancel()
                }
            }
        }
    }

    fun searchByName(value: String) {
        getSearchResult(value)
    }

    private val _progressBarVisibility = MutableLiveData(false)
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility.map { it }

    val groupEmptyListVisibility: LiveData<Boolean> = repositoryList.map { it.isEmpty() }
    val groupNotFoundVisibility: LiveData<Boolean> = responseEmpty.map { it == true }
    val recyclerVisibility: LiveData<Boolean> = repositoryList.map { !it.isNullOrEmpty() }

    fun addItemInDao(itemId: String) {
        val item = repositoryList.value?.find { item -> item.id == itemId }
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addItemInDao(mainRepository.transformItemInDao(item))
        }
    }

    fun clearFoundList() {
        repositoryList.postValue(ArrayList())
        responseEmpty.postValue(false)
        notifyDataSetChanged?.invoke()
    }

    var notifyDataSetChanged: (() -> Unit)? = null
}
