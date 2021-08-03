package com.atdev.githubproject

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.api.ApiService
import com.atdev.githubproject.datasource.RepositoryPagingDataSource
import com.atdev.githubproject.model.RepositoryCollectionEntity
import com.atdev.githubproject.room.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val repositoryDao: RepositoryDao,
    private val apiService: ApiService
) {

    fun getPagingDataByName(query:String): Flow<PagingData<RepositoryObjectDto>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { RepositoryPagingDataSource(apiService,query) }
        ).flow
    }

    fun getAllDownloadedRepository(): Flow<List<RepositoryCollectionEntity>> = repositoryDao.getAllDownloadedRepository()

    suspend fun addItemInDao(item: RepositoryCollectionEntity) {
        repositoryDao.addDownloadedRepository(item)
    }

    suspend fun deleteItemDao(item: RepositoryCollectionEntity) {
        repositoryDao.deleteDownloadedRepository(item)
    }

    suspend fun deleteAllDownloaded() {
        repositoryDao.deleteAllDownloaded()
    }
}