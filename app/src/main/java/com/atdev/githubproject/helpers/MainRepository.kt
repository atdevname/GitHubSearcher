package com.atdev.githubproject.helpers

import com.atdev.githubproject.model.RepositorySearchResult
import com.atdev.githubproject.model.RepositoryJsonObject
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

    suspend fun getSearchUser(value: String): Response<List<RepositoryJsonObject>> =
        apiService.searchUser(value)

    fun getAllDownloadedRepository(): Flow<List<EntityRepositoryDownloaded>> = repositoryDao.getAllDownloadedRepository()
    fun getAllBySearchRepository(): Flow<List<EntityRepositoryBySearch>> = repositoryDao.getAllBySearchRepository()
    fun getAllByUserRepository(): Flow<List<EntityRepositoryByUser>> = repositoryDao.getAllByUserRepository()

    fun transformItemInDao(item: RepositoryJsonObject?): EntityRepositoryDownloaded {
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