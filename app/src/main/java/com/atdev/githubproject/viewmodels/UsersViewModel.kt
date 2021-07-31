package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.retrofit.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var networkConnected = MutableLiveData<Boolean>()

    var repositoryList = MutableLiveData<List<RepositoryObjectDto>>(listOf())
    private var job: Job? = null
    private var responseEmpty = MutableLiveData(false)

    var foundByField = MutableLiveData("")

    private fun getSearchResult(value: String) {
        job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _progressBarVisibility.postValue(true)
                    val response = mainRepository.getSearchUser(value)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            repositoryList.postValue(it)
                            if (response.body()!!.isNotEmpty()) {
                                responseEmpty.postValue(false)
                                foundByField.postValue(value)
                            } else {
                                responseEmpty.postValue(true)
                            }
                        }
                    } else {
                        job?.cancel()
                    }
                } catch (e: NoConnectivityException) {
                    networkConnected.postValue(false)
                }
            }
            _progressBarVisibility.postValue(false)
        }
    }

    fun searchByName(value: String) {
        getSearchResult(value)
    }

    private val _progressBarVisibility = MutableLiveData<Boolean>(false)
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

    fun resetStatusAdded(itemId: String) {
        val item = repositoryList.value?.find { item -> item.id == itemId }
        item?.added = false
    }

    fun clearFoundList() {
        repositoryList.postValue(ArrayList())
        responseEmpty.postValue(false)
        notifyDataSetChanged?.invoke()
    }

    var notifyDataSetChanged: (() -> Unit)? = null
    var changeEmptyViews: (() -> Unit)? = null

}
