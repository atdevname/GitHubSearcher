package com.atdev.githubproject.search.api

import com.atdev.githubproject.search.model.RepositoryObjectDto
import com.atdev.githubproject.search.model.RepositorySearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{

    @GET("search/repositories?")
    suspend fun searchRepos(@Query("q") search: String): Response<RepositorySearchResult>

    @GET("users/{user}/repos")
    suspend fun searchUser(@Path("user") username: String): Response<List<RepositoryObjectDto>>

}