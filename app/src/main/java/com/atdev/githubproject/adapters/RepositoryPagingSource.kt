package com.atdev.githubproject.adapters

import androidx.paging.PagingSource
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.retrofit.ApiService

class RepositoryPagingSource(private val apiService: ApiService) : PagingSource<Int, RepositoryObjectDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryObjectDto> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getListData(currentLoadingPageKey)
            val responseData = mutableListOf<RepositoryObjectDto>()
            val data = response.body()?.myData ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}