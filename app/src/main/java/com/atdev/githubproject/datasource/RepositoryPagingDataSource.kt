package com.atdev.githubproject.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.api.ApiService
import com.atdev.githubproject.api.NetworkConnectionInterceptor
import com.atdev.githubproject.api.NoConnectivityException
import kotlinx.coroutines.*
import retrofit2.HttpException

class RepositoryPagingDataSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, RepositoryObjectDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryObjectDto> {
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        var loadResult: LoadResult<Int, RepositoryObjectDto>? = null

        coroutineScope {
            val pageNumber = params.key ?: 1

            val defResponse: Deferred<LoadResult<Int, RepositoryObjectDto>> =
                async(Dispatchers.IO) {
                    try {
                        val response = apiService.searchRepos(query)
                        return@async if (response.isSuccessful) {
                            val bodyResponse = response.body()!!.items

                            val nextKey = if (response.body()?.items!!.isEmpty()) null else pageNumber + 1
                            val prevKey = if (pageNumber > 1) pageNumber - 1 else null

                            LoadResult.Page(bodyResponse, prevKey, nextKey)
                        } else {
                            LoadResult.Error(HttpException(response))
                        }
                    } catch (e: NoConnectivityException) {
                        return@async LoadResult.Error(e)
                    }
                }
            loadResult = defResponse.await()
        }
        return loadResult!!
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryObjectDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
