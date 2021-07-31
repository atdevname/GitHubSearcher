package com.atdev.githubproject.helpers

import com.atdev.githubproject.model.RepositorySearchResult
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.retrofit.ApiService
import com.atdev.githubproject.room.EntityRepositoryBySearch
import com.atdev.githubproject.room.EntityRepositoryByUser
import com.atdev.githubproject.room.RepositoryDao
import com.atdev.githubproject.room.EntityRepositoryDownloaded
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

    fun getAllDownloadedRepository(): Flow<List<EntityRepositoryDownloaded>> = repositoryDao.getAllDownloadedRepository()

    fun transformItemInDao(item: RepositoryObjectDto?): EntityRepositoryDownloaded {
        return EntityRepositoryDownloaded(
            item!!.name,
            item.owner.login,
            item.owner.avatar_url,
            item.html_url,
            item.watchers_count,
            item.forks_count,
            item.language,
            item.id,
        )
    }

    suspend fun addItemInDao(item: EntityRepositoryDownloaded) {
        repositoryDao.addDownloadedRepository(item)
    }

    fun deleteItemDao(item: EntityRepositoryDownloaded) {
        repositoryDao.deleteDownloadedRepository(item)
    }
}