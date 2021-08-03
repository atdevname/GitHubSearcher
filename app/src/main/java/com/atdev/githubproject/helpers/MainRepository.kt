package com.atdev.githubproject.helpers

import com.atdev.githubproject.model.RepositorySearchResult
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.retrofit.ApiService
import com.atdev.githubproject.room.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val repositoryDao: RepositoryDao,
    private val apiService: ApiService
) {
    suspend fun getSearchRepository(value: String): Response<RepositorySearchResult> =
        apiService.searchRepos(value)

    suspend fun getSearchUser(value: String): Response<List<RepositoryObjectDto>> =
        apiService.searchUser(value)

    fun getAllDownloadedRepository(): Flow<List<RepositoryDownloadedEntity>> = repositoryDao.getAllDownloadedRepository()

    suspend fun addItemInDao(item: RepositoryDownloadedEntity) {
        repositoryDao.addDownloadedRepository(item)
    }

    suspend fun deleteItemDao(item: RepositoryDownloadedEntity) {
        repositoryDao.deleteDownloadedRepository(item)
    }

    suspend fun deleteAllDownloaded() {
        repositoryDao.deleteAllDownloaded()
    }
}