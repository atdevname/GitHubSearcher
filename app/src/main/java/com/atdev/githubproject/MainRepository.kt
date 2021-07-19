package com.atdev.githubproject

import com.atdev.githubproject.model.ReposResult
import com.atdev.githubproject.room.RepositoryDao
import com.atdev.githubproject.retrofit.ApiService
import com.atdev.githubproject.room.RepositoryEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val repositoryDao: RepositoryDao,
    private val apiService: ApiService
){
    suspend fun getSearchResult(value:String): Response<ReposResult> = apiService.searchRepos(value)

    fun getAllRepository(): Flow<List<RepositoryEntity>> = repositoryDao.getAllRepository()


}