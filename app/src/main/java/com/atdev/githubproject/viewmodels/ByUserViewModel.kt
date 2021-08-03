package com.atdev.githubproject.viewmodels

import androidx.lifecycle.*
import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.model.RepositoryObjectDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ByUserViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var networkConnected = MutableLiveData<Boolean>()

    var repositoryList = MutableLiveData<List<RepositoryObjectDto>>(listOf())
    private var job: Job? = null
    private var responseEmpty = MutableLiveData(false)

    var foundByField = MutableLiveData("")

    fun searchByName(value: String) {
    }

    private val _progressBarVisibility = MutableLiveData<Boolean>(false)
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility.map { it }

    val groupEmptyListVisibility: LiveData<Boolean> = repositoryList.map { it.isEmpty() }
    val groupNotFoundVisibility: LiveData<Boolean> = responseEmpty.map { it == true }
    val recyclerVisibility: LiveData<Boolean> = repositoryList.map { !it.isNullOrEmpty() }

    fun addItemInDao(item: RepositoryObjectDto) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addItemInDao(item.transformItemInDao())
        }
    }

    var notifyDataSetChanged: (() -> Unit)? = null
    var changeEmptyViews: (() -> Unit)? = null

}
