package com.atdev.githubproject.retrofit

import com.atdev.githubproject.model.ReposResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{

    @GET("search/repositories?")
    suspend fun searchRepos(@Query("q") search: String): Response<ReposResult>

}