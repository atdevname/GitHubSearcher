package com.atdev.githubproject.helpers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    fun getPagingDataByName(query:String): Flow<PagingData<RepositoryObjectDto>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { RepoPagingSourceByName(apiService,query) }
        ).flow
    }

    fun getPagingDataByUser(query:String): Flow<PagingData<RepositoryObjectDto>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { RepoPagingSourceByUser(apiService,query) }
        ).flow
    }


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