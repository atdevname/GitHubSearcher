package com.atdev.githubproject

import com.atdev.githubproject.collection.room.RepositoryDao
import com.atdev.githubproject.search.api.ApiService
import com.atdev.githubproject.search.model.RepositoryCollectionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val repositoryDao: RepositoryDao,
    private val apiService: ApiService
) {

    suspend fun getReposByName(query:String) = flowOf(apiService.searchRepos(query))

    fun getAllSavedRepository() = repositoryDao.getAllSavedReps()

    suspend fun saveRep(item: RepositoryCollectionEntity) =
        repositoryDao.saveRep(item)

    suspend fun deleteSavedRep(item: RepositoryCollectionEntity) =
        repositoryDao.deleteSavedRep(item)

    suspend fun deleteAllDownloaded() = repositoryDao.deleteAllSavedReps()
}